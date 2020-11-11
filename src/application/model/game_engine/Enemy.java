package application.model.game_engine;

import application.Main;
import application.resources.graphics.TexturePath;
import application.resources.sound.SFX;
import application.settings.AppSettings;
import application.settings.Dimensions;
import javafx.scene.image.Image;

import java.util.Random;

public class Enemy extends Player {
	public enum ENEMYTYPE {WORM, MONSTRO}
	
	private static Image up;
	private static Image down;
	private static Image left;
	private static Image right;
	
	private final ENEMYTYPE type;
	private int deathScore;
	private long lastDirectionChange;
	
	private boolean followingPath;
	private boolean moveVert;
	private boolean moveHor;
	private double goalX;
	private double goalY;
	private final double speed;
	
	public Enemy(ENEMYTYPE type, double v) {
		super();
		this.type = type;
		
		lastDirectionChange = System.currentTimeMillis();
		followingPath = false;
		
		if ( type == ENEMYTYPE.WORM ) {
			width = Dimensions.WORM_WIDTH;
			height = Dimensions.WORM_HEIGHT;
			deathScore = 300;
			health = 1;
			speedFactor = .65;
		} else if ( type == ENEMYTYPE.MONSTRO ) {
			width = Dimensions.MONSTRO_WIDTH;
			height = Dimensions.MONSTRO_HEIGHT;
			deathScore = 1000;
			health = 3;
			speedFactor = .5;
		}
		
		speed = v * speedFactor;
	}
	
	public int getDeathScore() {
		return deathScore;
	}
	
	public static void loadTextures() {
		up = new Image(TexturePath.monstro + "1.png", Dimensions.MONSTRO_WIDTH, Dimensions.MONSTRO_HEIGHT, true, true);
		down = new Image(TexturePath.monstro + "0.png", Dimensions.MONSTRO_WIDTH, Dimensions.MONSTRO_HEIGHT, true, true);
		left = new Image(TexturePath.worm + "1.png", Dimensions.WORM_WIDTH, Dimensions.WORM_HEIGHT, true, true);
		right = new Image(TexturePath.worm + "0.png", Dimensions.WORM_WIDTH, Dimensions.WORM_HEIGHT, true, true);
	}
	
	@Override
	public void update(double time) {
		//find a new position
		if ( !followingPath ) {
			Random pos = new Random();
			
			do {
				goalX = pos.nextDouble() * AppSettings.getWidth();
			} while (Math.abs(goalX - positionX) < width * 3);
			do {
				goalY = pos.nextDouble() * AppSettings.getHeight();
			} while (Math.abs(goalY - positionY) < height * 3);
			
			if ( goalX < AppSettings.getWidth() * .1 - width ) {
				goalX = AppSettings.getWidth() * .1 - width;
			} else if ( goalX > AppSettings.getWidth() * .9 ) {
				goalX = AppSettings.getWidth() * .9;
			}
			if ( goalY < AppSettings.getHeight() * .1 - height / 2 ) {
				goalY = AppSettings.getHeight() * .1 - height / 2;
			} else if ( goalY > AppSettings.getHeight() * .9 - height ) {
				goalY = AppSettings.getHeight() * .9 - height;
			}
			
			followingPath = true;
		}
		
		//randomize direction
		if ( lastDirectionChange <= System.currentTimeMillis() - 1000 ) {
			lastDirectionChange = System.currentTimeMillis();
			Random move = new Random();
			
			int dir = move.nextInt(3);
			
			switch (dir) {
				case 0:
					//vert
					moveVert = true;
					moveHor = move.nextBoolean();
					break;
				case 1:
					//hor
					moveHor = true;
					moveVert = move.nextBoolean();
					break;
				case 2:
					moveVert = moveHor = true;
					break;
			}
			
			if ( (int) positionX == (int) goalX ) {
				moveVert = true;
			} else if ( (int) positionY == (int) goalY ) {
				moveHor = true;
			}
		}
		
		//move if over threshold
		if ( moveHor && Math.abs((int) ( goalX - positionX )) > 10 * AppSettings.getSCALE() ) {
			if ( (int) positionX > (int) goalX ) {
				velocityX += -speed;
			} else if ( (int) positionX < (int) goalX ) {
				velocityX += speed;
			}
		}
		if ( moveVert && Math.abs((int) ( goalY - positionY )) > 10 * AppSettings.getSCALE() ) {
			if ( (int) positionY > (int) goalY ) {
				velocityY += -speed;
			} else if ( (int) positionY < (int) goalY ) {
				velocityY += speed;
			}
		}
		
		
		if ( velocityY != 0 && velocityX != 0 ) {
			velocityX /= Math.sqrt(2);
			velocityY /= Math.sqrt(2);
		}
		
		//set texture based on movement
		if ( ( type == ENEMYTYPE.WORM ) ) {
			if ( velocityX >= 0 ) {
				image = right;
			} else {
				image = left;
			}
		} else if ( type == ENEMYTYPE.MONSTRO ) {
			if ( velocityY >= 0 ) {
				image = down;
			} else {
				image = up;
			}
		}
		
		positionX += velocityX * time;
		positionY += velocityY * time;
		
		setVelocity(0, 0);
		
		//arrived at destination
		if ( Math.abs((int) ( goalX - positionX )) <= 10 * AppSettings.getSCALE() && Math.abs((int) ( goalY - positionY )) <= 10 * AppSettings.getSCALE() ) {
			followingPath = false;
			lastDirectionChange = 0;
		}
	}
	
	public void damage() {
		Main.audioEngine.playEffect(SFX.EFFECTS.HIT);
		--health;
	}
}
