package application.model.game_engine;

import application.settings.AppSettings;
import application.settings.Dimensions;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
	protected double positionX;
	protected double positionY;
	protected double velocityX;
	protected double velocityY;
	
	protected Image image;
	
	protected double width;
	protected double height;
	
	public Sprite() {
		positionX = 0;
		positionY = 0;
		velocityX = 0;
		velocityY = 0;
		width = 0;
		height = 0;
	}
	
	public void setImage(Image i) {
		image = i;
		width = i.getWidth();
		height = i.getHeight();
	}
	
	public void setImage(String filename) {
		Image i = new Image(filename, Dimensions.ISAAC_WIDTH, Dimensions.ISAAC_HEIGHT, true, true);
		setImage(i);
		width = i.getWidth();
		height = i.getHeight();
	}
	
	public void setPosition(double x, double y) {
		positionX = x;
		positionY = y;
	}
	
	public void setVelocity(double x, double y) {
		velocityX = x;
		velocityY = y;
	}
	
	public void addVelocity(double x, double y) {
		velocityX += x;
		velocityY += y;
	}
	
	public void update(double time) {
		if ( velocityY != 0 && velocityX != 0 ) {
			velocityX /= Math.sqrt(2);
			velocityY /= Math.sqrt(2);
		}
		
		positionX += velocityX * time;
		positionY += velocityY * time;
		
		//collide with vertical walls
		if ( positionX < AppSettings.getWidth() * .1 - width ) {
			positionX = AppSettings.getWidth() * .1 - width;
		} else if ( positionX > AppSettings.getWidth() * .9 ) {
			positionX = AppSettings.getWidth() * .9;
		}
		
		//collide with horizontal walls
		if ( positionY < AppSettings.getHeight() * .1 - height / 2 ) {
			positionY = AppSettings.getHeight() * .1 - height / 2;
		} else if ( positionY > AppSettings.getHeight() * .95 - height ) {
			positionY = AppSettings.getHeight() * .95 - height;
		}
	}
	
	public void render(GraphicsContext gc) {
		gc.drawImage(image, positionX, positionY);
	}
	
	public void hitbox(GraphicsContext gc) {
		gc.strokeRect(positionX, positionY, width, height);
	}
	
	public Rectangle2D getBoundary() {
		return new Rectangle2D(positionX, positionY, width, height);
	}
	
	public boolean intersects(Sprite s) {
		return s.getBoundary().intersects(this.getBoundary());
	}
	
	public String toString() {
		return " Position: [" + positionX + "," + positionY + "]" + " Velocity: [" + velocityX + "," + velocityY + "]";
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
	
	public void setWidth(double w) {
		width = w;
	}
	
	public void setHeight(double h) {
		height = h;
	}
}