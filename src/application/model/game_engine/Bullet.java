package application.model.game_engine;

import application.resources.graphics.TexturePath;
import application.settings.AppSettings;
import application.settings.Dimensions;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Pair;

public class Bullet extends Sprite {
	public static abstract class Direction {
		public static final Pair<Integer, Integer> UP = new Pair<>(0, -1);
		public static final Pair<Integer, Integer> LEFT = new Pair<>(-1, 0);
		public static final Pair<Integer, Integer> DOWN = new Pair<>(0, 1);
		public static final Pair<Integer, Integer> RIGHT = new Pair<>(1, 0);
	}
	
	private static Image bullet;
	private boolean active;
	private final double startX;
	private final double startY;
	
	public Bullet(Pair<Integer, Integer> direction, double speedX, double speedY, double speed, double positionX, double positionY) {
		super();
		setImage(bullet);
		
		this.positionX = positionX + Dimensions.ISAAC_WIDTH / 2;
		this.positionY = positionY + Dimensions.ISAAC_HEIGHT / 2;
		
		startX = positionX;
		startY = positionY;
		
		active = true;
		
		velocityX = speedX + speed * direction.getKey();
		velocityY = speedY + speed * direction.getValue();
	}
	
	public static void loadTexture(Player.PLAYERTYPE playertype) {
		if ( playertype == Player.PLAYERTYPE.ISAAC ) {
			bullet = new Image(TexturePath.isaac + "tear.png", 16 * AppSettings.getSCALE(), 16 * AppSettings.getSCALE(), true, true);
		} else if ( playertype == Player.PLAYERTYPE.DARKISAAC ) {
			bullet = new Image(TexturePath.darkisaac + "tear.png", 16 * AppSettings.getSCALE(), 16 * AppSettings.getSCALE(), true, true);
		}
	}
	
	@Override
	public void update(double time) {
		positionX += velocityX * time;
		positionY += velocityY * time;
		
		if ( positionX >= AppSettings.getWidth() || positionX <= 0 ) {
			setActive(false);
		}
		if ( positionY >= AppSettings.getHeight() || positionY <= 0 ) {
			setActive(false);
		}
	}
	
	@Override
	public boolean intersects(Sprite s) {
		return super.intersects(s) && active;
	}
	
	@Override
	public void render(GraphicsContext gc) {
		gc.drawImage(bullet, positionX, positionY);
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public double getStartX() {
		return startX;
	}
	
	public double getStartY() {
		return startY;
	}
}
