package application.model.game_engine;

import application.resources.graphics.TexturePath;
import application.settings.Dimensions;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Door extends Sprite {
	private static Image texture;
	private boolean unlocked;
	private final String way;
	
	public Door(double posX, double posY, String _way) {
		super();
		setImage(texture);
		positionX = posX;
		positionY = posY;
		unlocked = false;
		way = _way;
	}
	
	public static void loadTexture() {
		texture = new Image(TexturePath.door, Dimensions.DOOR_DIM, Dimensions.DOOR_DIM, true, true);
	}
	
	public static Door safeArea(Player.Direction dir) {
		Door area = null;
		double m = 4.5;
		switch (dir) {
			case UP:
				area = new Door(Dimensions.VER_DOOR_X, Dimensions.UP_DOOR_Y, "");
				area.setWidth(area.width * ( m / 2 ));
				area.setHeight(area.height * m);
				area.setPosition(area.positionX - ( area.width / 2 ), area.positionY - ( area.height / 2 ));
				break;
			case DOWN:
				area = new Door(Dimensions.VER_DOOR_X, Dimensions.DOWN_DOOR_Y, "");
				area.setWidth(area.width * ( m / 2 ));
				area.setHeight(area.height * m);
				area.setPosition(area.positionX - ( area.width / 2 ), area.positionY - ( area.height / 2 ));
				break;
			case RIGHT:
				area = new Door(Dimensions.RIGHT_DOOR_X, Dimensions.HOR_DOOR_Y, "");
				area.setWidth(area.width * m);
				area.setHeight(area.height * ( m / 2 ));
				area.setPosition(area.positionX - ( area.width / 2 ), area.positionY - ( area.height / 2 ));
				break;
			case LEFT:
				area = new Door(Dimensions.LEFT_DOOR_X, Dimensions.HOR_DOOR_Y, "");
				area.setWidth(area.width * m);
				area.setHeight(area.height * ( m / 2 ));
				area.setPosition(area.positionX - ( area.width / 2 ), area.positionY - ( area.height / 2 ));
				break;
		}
		return area;
	}
	
	@Override
	public void render(GraphicsContext gc) {
		if ( unlocked ) {
			gc.drawImage(texture, positionX, positionY);
		}
	}
	
	public void setUnlocked() {
		unlocked = true;
	}
	
	public String getWay() {
		return way;
	}
}
