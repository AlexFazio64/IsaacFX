package application.model.game_engine;

import application.model.room_engine.Room;
import application.settings.AppSettings;
import application.settings.Dimensions;

import java.util.ArrayList;
import java.util.Random;

public class PlayableRoom extends Room {
	public final ArrayList<Door> doors;
	public final ArrayList<Enemy> enemies;
	public final ArrayList<Bullet> bullets;
	private boolean clear;
	
	public PlayableRoom(Room room) {
		super(0, 0);
		super.setId(room.getId());
		
		enemies = new ArrayList<>();
		doors = new ArrayList<>();
		bullets = new ArrayList<>();
		clear = false;
	}
	
	public boolean cleared() {
		return enemies.isEmpty() || clear;
	}
	
	public boolean isClear() {
		return clear;
	}
	
	public void unlockDoors() {
		if ( doors.isEmpty() ) {
			if ( canGoUp() ) {
				Door up = new Door(Dimensions.VER_DOOR_X - Dimensions.DOOR_DIM / 2, Dimensions.UP_DOOR_Y - Dimensions.DOOR_DIM / 1.9, "up");
				doors.add(up);
			}
			if ( canGoDown() ) {
				Door down = new Door(Dimensions.VER_DOOR_X - Dimensions.DOOR_DIM / 2, Dimensions.DOWN_DOOR_Y - Dimensions.DOOR_DIM / 2.1, "down");
				doors.add(down);
			}
			if ( canGoLeft() ) {
				Door left = new Door(Dimensions.LEFT_DOOR_X - Dimensions.DOOR_DIM * .25, Dimensions.HOR_DOOR_Y - Dimensions.DOOR_DIM / 2, "left");
				doors.add(left);
			}
			if ( canGoRight() ) {
				Door right = new Door(Dimensions.RIGHT_DOOR_X - Dimensions.DOOR_DIM * .75, Dimensions.HOR_DOOR_Y - Dimensions.DOOR_DIM / 2, "right");
				doors.add(right);
			}
		}
		doors.forEach(Door::setUnlocked);
		clear = true;
	}
	
	public void fillEnemies(int hostiles, Random generator, double v) {
		//don't spawn in safe area
		if ( canGoUp() ) {
			doors.add(Door.safeArea(Player.Direction.UP));
		}
		if ( canGoDown() ) {
			doors.add(Door.safeArea(Player.Direction.DOWN));
		}
		if ( canGoRight() ) {
			doors.add(Door.safeArea(Player.Direction.RIGHT));
		}
		if ( canGoLeft() ) {
			doors.add(Door.safeArea(Player.Direction.LEFT));
		}
		
		for (int i = 0; i < hostiles; ++i) {
			int type = generator.nextInt(5);
			Enemy a = new Enemy(type <= 2 ? Enemy.ENEMYTYPE.WORM : Enemy.ENEMYTYPE.MONSTRO, v);
			boolean spawn;
			do {
				spawn = false;
				a.setPosition(a.getWidth() + generator.nextDouble() * AppSettings.getWidth() * .8, a.getHeight() + generator.nextDouble() * AppSettings.getHeight() * .8);
				for (Enemy enemy: enemies)
					if ( a.intersects(enemy) ) {
						spawn = true;
						break;
					}
				
				if ( !spawn ) {
					for (Door door: doors)
						if ( a.intersects(door) ) {
							spawn = true;
							break;
						}
				}
			} while (spawn);
			enemies.add(a);
		}
		
		//delete safe area
		doors.clear();
	}
}
