package application.model.game_engine;

import application.Main;
import application.resources.graphics.TexturePath;
import application.resources.sound.SFX;
import application.settings.AppSettings;
import application.settings.Dimensions;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Stack;

public class Player extends Sprite {
	public enum PLAYERTYPE {ISAAC, DARKISAAC}
	
	public enum Direction {UP, DOWN, LEFT, RIGHT, IDLE}
	
	protected int health;
	protected boolean vulnerable;
	protected double speedFactor;
	private double attackSpeed;
	private double lastShot;
	private double lastDamaged;
	
	private static ArrayList<Image> up;
	private static ArrayList<Image> down;
	private static ArrayList<Image> left;
	private static ArrayList<Image> right;
	
	private final int[] animationFrame;
	private Direction direction;
	
	public Player() {
		super();
		vulnerable = true;
		animationFrame = new int[0];
	}
	
	public Player(PLAYERTYPE type) {
		super();
		width = Dimensions.ISAAC_WIDTH;
		height = Dimensions.ISAAC_HEIGHT;
		setPosition(AppSettings.getWidth() / 2 - width, AppSettings.getHeight() / 2 - height);
		
		vulnerable = true;
		animationFrame = new int[]{1, 1, 1, 1};
		direction = Direction.IDLE;
		
		if ( type == PLAYERTYPE.ISAAC ) {
			health = 5;
			speedFactor = 1.1;
			attackSpeed = 1.7;
		} else if ( type == PLAYERTYPE.DARKISAAC ) {
			health = 4;
			speedFactor = .85;
			attackSpeed = 2.4;
		}
		lastShot = System.currentTimeMillis();
		lastDamaged = System.currentTimeMillis();
	}
	
	public static void loadTextures(PLAYERTYPE playertype) {
		up = new ArrayList<>();
		down = new ArrayList<>();
		left = new ArrayList<>();
		right = new ArrayList<>();
		
		String path = playertype == PLAYERTYPE.ISAAC ? TexturePath.isaac : TexturePath.darkisaac;
		
		for (int i = 0; i < 3; i++) {
			up.add(new Image(path + "up/" + i + ".png", Dimensions.ISAAC_WIDTH, Dimensions.ISAAC_HEIGHT, true, true));
			down.add(new Image(path + "down/" + i + ".png", Dimensions.ISAAC_WIDTH, Dimensions.ISAAC_HEIGHT, true, true));
			left.add(new Image(path + "left/" + i + ".png", Dimensions.ISAAC_WIDTH, Dimensions.ISAAC_HEIGHT, true, true));
			right.add(new Image(path + "right/" + i + ".png", Dimensions.ISAAC_WIDTH, Dimensions.ISAAC_HEIGHT, true, true));
		}
	}
	
	@Override
	public void addVelocity(double x, double y) {
		super.addVelocity(x * speedFactor, y * speedFactor);
	}
	
	public Bullet shoot(Pair<Integer, Integer> direction, double speed) {
		Main.audioEngine.playEffect(SFX.EFFECTS.SHOT);
		lastShot = System.currentTimeMillis();
		return new Bullet(direction, velocityX * .4, velocityY * .4, speed, positionX, positionY);
	}
	
	public boolean canShoot() {
		return lastShot <= System.currentTimeMillis() - 1000 / attackSpeed;
	}
	
	public boolean intersects(Enemy s) {
		return new Rectangle2D(positionX + ( width * .2 ) / 2, positionY + height * .4, width * .8, height * .6).intersects(s.getBoundary());
	}
	
	@Override
	public void hitbox(GraphicsContext gc) {
		gc.strokeRect(positionX + ( width * .2 ) / 2, positionY + height * .4, width * .8, height * .6);
	}
	
	@Override
	public void update(double time) {
		if ( lastDamaged <= System.currentTimeMillis() - 1500 ) {
			vulnerable = true;
		}
		
		super.update(time);
		
		if ( velocityX > 0 ) {
			direction = Direction.RIGHT;
		} else if ( velocityX < 0 ) {
			direction = Direction.LEFT;
		} else if ( velocityX == 0 ) {
			if ( velocityY == 0 ) {
				direction = Direction.IDLE;
			} else if ( velocityY < 0 ) {
				direction = Direction.UP;
			} else {
				direction = Direction.DOWN;
			}
		}
		
		switch (direction) {
			case UP:
				++animationFrame[0];
				animationFrame[1] = 1;
				animationFrame[2] = 1;
				animationFrame[3] = 1;
				image = up.get(( animationFrame[0] - 1 ) / 20);
				break;
			case DOWN:
				++animationFrame[1];
				animationFrame[0] = 0;
				animationFrame[2] = 0;
				animationFrame[3] = 0;
				image = down.get(animationFrame[1] / 20);
				break;
			case LEFT:
				++animationFrame[2];
				animationFrame[0] = 0;
				animationFrame[1] = 0;
				animationFrame[3] = 0;
				image = left.get(animationFrame[2] / 20);
				break;
			case RIGHT:
				++animationFrame[3];
				animationFrame[1] = 0;
				animationFrame[2] = 0;
				animationFrame[0] = 0;
				image = right.get(animationFrame[3] / 20);
				break;
			case IDLE:
				animationFrame[0] = 0;
				animationFrame[1] = 0;
				animationFrame[2] = 0;
				animationFrame[3] = 0;
				image = down.get(0);
				break;
		}
		for (int i = 0; i < 4; i++)
			if ( animationFrame[i] >= 59 ) {
				animationFrame[i] = 0;
			}
	}
	
	public void damage(Stack<Sprite> hud) {
		if ( vulnerable ) {
			lastDamaged = System.currentTimeMillis();
			vulnerable = false;
			--health;
			Main.audioEngine.playEffect(SFX.EFFECTS.HURT);
			hud.pop();
		}
	}
	
	public int getHealth() {
		return health;
	}
	
	public boolean isDead() {
		return health == 0;
	}
	
	public void giveInvincibilityFrames() {
		this.lastDamaged = System.currentTimeMillis();
		vulnerable = false;
	}
	
	public void giveExtraLife(Stack<Sprite> HUD) {
		if ( health == 10 ) {
			return;
		}
		Sprite heart = new Sprite();
		heart.setImage(TexturePath.darkisaac + "hearts.png");
		heart.setPosition(health * Dimensions.HEART_DIM, 0);
		HUD.add(heart);
		health++;
	}
}
