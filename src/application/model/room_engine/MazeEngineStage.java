package application.model.room_engine;

import application.Main;
import application.controller.RoomEngineController;
import application.resources.sound.SFX;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MazeEngineStage extends Stage {
	private final RoomEngineController controller;
	
	public MazeEngineStage(Scene room_scene, RoomEngineController _controller) {
		super();
		setScene(room_scene);
		setTitle("Create Mode");
		setResizable(false);
		initModality(Modality.APPLICATION_MODAL);
		this.controller = _controller;
		this.setOnCloseRequest(e -> Main.audioEngine.playBackground(SFX.BACKGROUND.MENU, true));
		this.setAlwaysOnTop(true);
	}
	
	public void createMaze() {
		controller.init();
		this.showAndWait();
	}
	
	public Seed generateMazeNoGraphics() {
		return controller.randomizeSeedNoGraphics();
	}
	
	public Seed challengeSeedNoGraphics(Pair<Integer, Seed> pair) {
		return controller.challengeSeedNoGraphics(pair);
	}
}
