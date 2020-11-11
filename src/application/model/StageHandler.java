package application.model;

import application.Main;
import application.controller.Controller;
import application.controller.LeaderboardController;
import application.controller.RoomEngineController;
import application.model.room_engine.MazeEngineStage;
import application.model.room_engine.Seed;
import application.resources.sound.SFX;
import application.settings.AppSettings;
import application.settings.SCENES;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class StageHandler {
	private static Stage mainStage;
	private static MazeEngineStage generationEngine;
	private static Map<String, Scene> sceneMap = new HashMap<>();
	private static Map<String, Controller> controllerMap = new HashMap<>();
	
	//MAIN STAGE
	public static void setStage(Stage stage) {
		mainStage = stage;
	}
	
	public static void setControllerMap(Map<String, Controller> map) {
		controllerMap = map;
	}
	
	public static void initialize() {
		controllerMap.values().forEach(Controller::init);
	}
	
	public static void initialize(String... controllers) {
		for (String c: controllers)
			controllerMap.get(c).init();
	}
	
	public static void localize() {
		controllerMap.values().forEach(Controller::localize);
	}
	
	public static void refreshSettings() {
		controllerMap.values().forEach(Controller::refreshSettings);
	}
	
	public static void readFromServer() {
		LeaderboardController lb = (LeaderboardController) controllerMap.get("leaderboard");
		lb.downloadLeaderboard();
	}
	
	public static void setSceneHandler(Map<String, Scene> handler) {
		sceneMap = handler;
	}
	
	public static void setScene(String s) {
		if ( mainStage.getScene() == sceneMap.get(s) ) {
			return;
		}
		
		mainStage.hide();
		mainStage.setScene(sceneMap.get(s));
		
		mainStage.setWidth(AppSettings.getWidth());
		mainStage.setHeight(AppSettings.getHeight());
		
		if ( s.contains("fhd") || s.contains("board") || s.contains("comma") || s.contains("game") ) {
			setFullscreen();
		} else {
			setWindowed();
		}
		
		mainStage.show();
		mainStage.requestFocus();
	}
	
	public static void backToMenu() {
		Main.audioEngine.playBackground(SFX.BACKGROUND.MENU, true);
		StageHandler.refreshSettings();
		if ( AppSettings.getSCALE() == AppSettings.STD ) {
			StageHandler.setScene(SCENES.MENU_STD);
		} else if ( AppSettings.getSCALE() == AppSettings.HD ) {
			StageHandler.setScene(SCENES.MENU_HD);
		} else if ( AppSettings.getSCALE() == AppSettings.FHD ) {
			StageHandler.setScene(SCENES.MENU_FHD);
		}
	}
	
	public static void backToOptions() {
		StageHandler.refreshSettings();
		if ( AppSettings.getSCALE() == AppSettings.STD ) {
			StageHandler.setScene(SCENES.OPTIONS_STD);
		} else if ( AppSettings.getSCALE() == AppSettings.HD ) {
			StageHandler.setScene(SCENES.OPTIONS_HD);
		} else if ( AppSettings.getSCALE() == AppSettings.FHD ) {
			StageHandler.setScene(SCENES.OPTIONS_FHD);
		}
	}
	
	private static void setFullscreen() {
		mainStage.setFullScreen(AppSettings.getSCALE() == AppSettings.FHD);
	}
	
	private static void setWindowed() {
		if ( AppSettings.getSCALE() != AppSettings.FHD ) {
			mainStage.setFullScreen(false);
		}
	}
	
	//OTHER STAGE
	public static void setGenerationEngine(Scene s, RoomEngineController p) {
		generationEngine = new MazeEngineStage(s, p);
	}
	
	public static void hideGenerator() {
		generationEngine.close();
	}
	
	public static void showGenerator() {
		Main.audioEngine.playBackground(SFX.BACKGROUND.CREATION, true);
		generationEngine.createMaze();
	}
	
	public static Seed getRandomSeed() {
		return generationEngine.generateMazeNoGraphics();
	}
	
	public static Seed challengeSeed(Pair<Integer, Seed> pair) {
		return generationEngine.challengeSeedNoGraphics(pair);
	}
	
	public static void endGame() {
		Main.gameController.endGame();
	}
}
