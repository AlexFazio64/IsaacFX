package application.model.room_engine;

import application.controller.RoomEngineController;
import application.resources.graphics.TexturePath;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class GameRoom extends Canvas {
	private static ArrayList<Image> rooms;
	private static int id = 0;
	
	private Image texture;
	private int room_type;
	private final int local_id;
	
	public static void init(double width, double height) {
		rooms = new ArrayList<>();
		
		for (int i = 0; i < 16; ++i) {
			rooms.add(new Image(TexturePath.rooms + Seed.decToBin(i) + ".png", width, height, true, true));
		}
	}
	
	public GameRoom(double width, double height, String room_id) {
		super(width, height);
		local_id = id++;
		
		this.room_type = Seed.binToDec(room_id);
		this.getGraphicsContext2D().setFill(Color.web("#000000"));
		
		if ( room_type != 0 ) {
			this.texture = rooms.get(room_type);
			this.getGraphicsContext2D().drawImage(texture, 0, 0);
		} else {
			this.getGraphicsContext2D().fillRect(0, 0, width, height);
		}
		
		this.setOnMouseClicked(e -> {
			if ( e.getButton() == MouseButton.PRIMARY ) {
				++room_type;
				room_type %= rooms.size();
			} else if ( e.getButton() == MouseButton.SECONDARY ) {
				--room_type;
				if ( room_type < 0 ) {
					room_type = rooms.size() - 1;
				}
			}
			changeRoom();
		});
	}
	
	private void changeRoom() {
		if ( room_type != 0 ) {
			this.texture = rooms.get(room_type);
			this.getGraphicsContext2D().drawImage(texture, 0, 0);
		} else {
			this.getGraphicsContext2D().fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		RoomEngineController.getSeed().parseRoom(this);
		RoomEngineController.getSeed().printCurrent();
	}
	
	public int getRoom_type() {
		return room_type;
	}
	
	public int getLocal_id() {
		return local_id;
	}
	
	public static void resetID() {
		id = 0;
	}
	
	public void markStart() {
		this.getGraphicsContext2D().fillOval(getWidth() / 2 - 2.5, getHeight() / 2 - 2.5, 10, 10);
	}
	
	public void markEnd() {
		this.getGraphicsContext2D().setFill(Color.web("#FF0000"));
		this.getGraphicsContext2D().fillOval(getWidth() / 2 - 2.5, getHeight() / 2 - 2.5, 10, 10);
		this.getGraphicsContext2D().setFill(Color.web("#000000"));
	}
	
	public Image getTexture() {
		return texture;
	}
}
