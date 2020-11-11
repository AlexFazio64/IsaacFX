package application.model.game_engine;

import application.Main;
import application.model.StageHandler;
import application.model.network.Client;
import application.model.network.Score;
import application.model.room_engine.Room;
import application.model.room_engine.Seed;
import application.resources.graphics.TexturePath;
import application.resources.sound.SFX;
import application.settings.AppSettings;
import application.settings.Dimensions;
import application.settings.KEYMAP;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class GameLoop extends AnimationTimer {
	private final GraphicsContext g;
	private ArrayList<KeyCode> input;
	private Image roomTexture;
	private Image endScreen;
	private long lastFrame;
	
	private PlayableRoom[][] maze;
	private Seed seed;
	private int dim;
	public double score;
	
	private int curr_row;
	private int curr_col;
	private int goal_row;
	private int goal_col;
	
	private Stack<Sprite> HUD;
	private Player isaac;
	private double speed;
	private boolean hurt;
	
	public GameLoop(GraphicsContext _graphics) {
		super();
		this.g = _graphics;
		g.setLineWidth(3);
		input = new ArrayList<>();
	}
	
	public void play(Player.PLAYERTYPE playertype) {
		HUD = new Stack<>();
		
		//load textures
		endScreen = new Image(TexturePath.endScreen, AppSettings.getWidth(), AppSettings.getHeight(), true, true);
		Player.loadTextures(playertype);
		Bullet.loadTexture(playertype);
		Enemy.loadTextures();
		Door.loadTexture();
		
		//GET POSITIONS
		curr_row = Main.roomController.getStartPosition()[0];
		curr_col = Main.roomController.getStartPosition()[1];
		goal_row = Main.roomController.getEndPosition()[0];
		goal_col = Main.roomController.getEndPosition()[1];
		
		//get SEED
		seed = Main.gameController.getCurrentSeed();
		dim = Main.roomController.getDimension();
		score = 0;
		Random gen = new Random(seed.toString().hashCode());
		
		//SPAWN PLAYER
		isaac = new Player(playertype);
		speed = 600 / AppSettings.FHD * AppSettings.getSCALE();
		
		//CONSTRUCT MAZE
		maze = new PlayableRoom[dim][dim];
		Room[][] temp = seed.getMap(dim);
		
		for (int i = 0; i < dim; ++i) {
			for (int j = 0; j < dim; ++j) {
				maze[i][j] = new PlayableRoom(temp[i][j]);
				if ( i == curr_row && j == curr_col ) {
					maze[i][j].setType(Room.RoomType.START);
					continue;
				}
				if ( i == goal_row && j == goal_col ) {
					maze[i][j].fillEnemies(gen.nextInt(3) + 5, gen, speed * 1.1);
					maze[i][j].setType(Room.RoomType.END);
				} else {
					maze[i][j].fillEnemies(gen.nextInt(3) + 3, gen, speed);
					maze[i][j].setType(Room.RoomType.ENEMY);
				}
			}
		}
		
		//SETUP HUD
		for (int i = 0; i < isaac.getHealth(); i++) {
			Sprite heart = new Sprite();
			heart.setImage(TexturePath.darkisaac + "hearts.png");
			heart.setPosition(i * Dimensions.HEART_DIM, 0);
			HUD.add(heart);
		}
		
		input.clear();
		hurt = true;
		setRoom();
		
		//choose music
		int music = gen.nextInt(3);
		String song = null;
		switch (music) {
			case 0:
				song = SFX.GAME.EASY_BATTLE;
				break;
			case 1:
				song = SFX.GAME.NORMAL_BATTLE;
				break;
			case 2:
				song = SFX.GAME.HARD_BATTLE;
				break;
		}
//		System.out.println(song);
		Main.audioEngine.playBackground(song, true);
		
		this.lastFrame = System.nanoTime();
		this.start();
	}
	
	@Override
	public void handle(long now) {
		//frame timing
		double t = ( now - lastFrame ) / 1000000000.0;
		lastFrame = now;
		
		PlayableRoom currentRoom = maze[curr_row][curr_col];
		
		//exit
		if ( input.contains(KEYMAP.MENU_BACK) ) {
			isaac.health = 0;
			stopGame();
			return;
		}
		
		//win condition
		if ( currentRoom.cleared() ) {
			if ( currentRoom.getType() != Room.RoomType.START && !currentRoom.isClear() ) {
				Main.audioEngine.playEffect(SFX.EFFECTS.UNLOCK);
			}
			currentRoom.unlockDoors();
			
			if ( curr_row == goal_row && curr_col == goal_col ) {
				stopGame();
				return;
			}
		}
		
		//room transition
		for (int i = 0; i < currentRoom.doors.size(); ++i) {
			double nx = 0, ny = 0;
			Door d = currentRoom.doors.get(i);
			if ( isaac.intersects(d) ) {
				switch (d.getWay()) {
					case "up":
						nx = Dimensions.VER_DOOR_X - isaac.width / 2;
						ny = Dimensions.DOWN_DOOR_Y - d.width - isaac.height;
						--curr_row;
						break;
					case "down":
						nx = Dimensions.VER_DOOR_X - isaac.width / 2;
						ny = Dimensions.UP_DOOR_Y + d.width * .9;
						++curr_row;
						break;
					case "left":
						nx = Dimensions.RIGHT_DOOR_X - d.width - isaac.width;
						ny = Dimensions.HOR_DOOR_Y - isaac.height / 2;
						--curr_col;
						break;
					case "right":
						nx = Dimensions.LEFT_DOOR_X + d.width * .9;
						ny = Dimensions.HOR_DOOR_Y - isaac.height / 2;
						++curr_col;
						break;
				}
				
				//teleport player
				isaac.setVelocity(0, 0);
				isaac.setPosition(nx, ny);
				isaac.update(t);
				
				//change room
				setRoom();
				break;
			}
		}
		
		//player movement
		isaac.setVelocity(0, 0);
		if ( input.contains(KEYMAP.GAME_UP) ) {
			isaac.addVelocity(0, -speed);
		}
		if ( input.contains(KEYMAP.GAME_DOWN) ) {
			isaac.addVelocity(0, speed);
		}
		if ( input.contains(KEYMAP.GAME_LEFT) ) {
			isaac.addVelocity(-speed, 0);
		}
		if ( input.contains(KEYMAP.GAME_RIGHT) ) {
			isaac.addVelocity(speed, 0);
		}
		
		//shooting
		if ( isaac.canShoot() ) {
			if ( input.contains(KEYMAP.GAME_SHOOT_UP) ) {
				currentRoom.bullets.add(isaac.shoot(Bullet.Direction.UP, speed * 2));
			} else if ( input.contains(KEYMAP.GAME_SHOOT_DOWN) ) {
				currentRoom.bullets.add(isaac.shoot(Bullet.Direction.DOWN, speed * 2));
			} else if ( input.contains(KEYMAP.GAME_SHOOT_LEFT) ) {
				currentRoom.bullets.add(isaac.shoot(Bullet.Direction.LEFT, speed * 2));
			} else if ( input.contains(KEYMAP.GAME_SHOOT_RIGHT) ) {
				currentRoom.bullets.add(isaac.shoot(Bullet.Direction.RIGHT, speed * 2));
			}
		}
		
		//enemy handling
		for (int i = 0; i < currentRoom.bullets.size(); ++i) {
			Bullet b = currentRoom.bullets.get(i);
			for (int j = 0; j < currentRoom.enemies.size(); ++j) {
				Enemy e = currentRoom.enemies.get(j);
				if ( b.intersects(e) ) {
					e.damage();
					b.setActive(false);
					
					//remove if dead
					if ( e.isDead() ) {
						double mult = findDistance(b, e) / ( 100 * AppSettings.getSCALE() );
//						System.out.println("multiplier: " + mult);
						double temp = e.getDeathScore() * mult;
//						System.out.println("+" + temp);
						score += temp;
						currentRoom.enemies.remove(e);
					}
					break;
				}
			}
		}
		currentRoom.enemies.forEach(e -> e.update(t));
		
		//damage player on touch
		currentRoom.enemies.forEach(e -> {
			if ( isaac.intersects(e) ) {
				isaac.damage(HUD);
				hurt = true;
			}
		});
		
		//lose condition
		if ( isaac.isDead() ) {
			stopGame();
			return;
		}
		
		//update bullets
		currentRoom.bullets.removeIf(bullet -> !bullet.isActive());
		currentRoom.bullets.forEach(b -> b.update(t));
		
		//rendering
		g.clearRect(0, 0, AppSettings.getWidth(), AppSettings.getHeight());
		g.drawImage(roomTexture, 0, 0);
		
		
		currentRoom.bullets.forEach(b -> b.render(g));
		currentRoom.enemies.forEach(e -> e.render(g));
		currentRoom.doors.forEach(d -> d.render(g));
		
		isaac.update(t);
		isaac.render(g);
//		isaac.hitbox(g);
		
		//draw HUD on top
		HUD.forEach(h -> h.render(g));
	}
	
	private double findDistance(Bullet bullet, Enemy enemy) {
		double a = Math.abs(bullet.getStartX() - enemy.positionX);
		double b = Math.abs(bullet.getStartY() - enemy.positionY);
		
		return Math.sqrt(( Math.pow(a, 2) + Math.pow(b, 2) ));
	}
	
	private void stopGame() {
		g.drawImage(endScreen, 0, 0);
		stop();
		
		if ( !isaac.isDead() ) {
			Main.audioEngine.playEffect(SFX.EFFECTS.STAGE_CLEAR);
			uploadGame();
		} else {
			Main.audioEngine.playEffect(SFX.EFFECTS.DEAD);
		}
		
		backToMenu();
	}
	
	private void uploadGame() {
		String username = System.getProperty("user.name");
		Score entry = new Score(username, seed.toString(), (int) score, dim);
		Client.save(entry);
	}
	
	private void backToMenu() {
		StageHandler.endGame();
	}
	
	private void setRoom() {
		isaac.giveInvincibilityFrames();
		if ( !hurt && !maze[curr_row][curr_col].isClear() && curr_row != Main.roomController.getStartPosition()[0] && curr_col != Main.roomController.getStartPosition()[1] ) {
			isaac.giveExtraLife(HUD);
		}
		this.roomTexture = seed.getRoom(curr_row, curr_col);
		hurt = false;
	}
	
	public void getInput(ArrayList<KeyCode> input) {
		this.input = input;
	}
}