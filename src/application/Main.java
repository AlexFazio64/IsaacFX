package application;

import application.controller.Controller;
import application.controller.GameController;
import application.controller.RoomEngineController;
import application.model.StageHandler;
import application.model.game_engine.GameLoop;
import application.resources.sound.AudioEngine;
import application.resources.sound.SFX;
import application.settings.AppSettings;
import application.settings.SCENES;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {
	public static GameController gameController;
	public static RoomEngineController roomController;
	public static AudioEngine audioEngine;
	public static GameLoop GAME;
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		StageHandler.setStage(primaryStage);
		Map<String, Scene> sceneMap = new HashMap<>();
		Map<String, Controller> controllers = new HashMap<>();
		
		//FX fluff
		primaryStage.setTitle("Isaac Project");
		primaryStage.setFullScreenExitHint("");
		primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		
		//load user preferences
		AppSettings.loadSettings();
		
		//load game screen
		loadGame(sceneMap);
		
		//load scenes
		loadMenu(SCENES.MENU_STD, sceneMap, controllers);
		loadMenu(SCENES.MENU_HD, sceneMap, controllers);
		loadMenu(SCENES.MENU_FHD, sceneMap, controllers);
		loadOptions(SCENES.OPTIONS_STD, sceneMap, controllers);
		loadOptions(SCENES.OPTIONS_HD, sceneMap, controllers);
		loadOptions(SCENES.OPTIONS_FHD, sceneMap, controllers);
		
		//load submenus
		loadLeaderboard(sceneMap, controllers);
		loadCharSelection(sceneMap, controllers);
		loadCommands(sceneMap, controllers);
		
		//load room generator
		loadRoomGen();
		
		//initialize all controllers
		StageHandler.setSceneHandler(sceneMap);
		StageHandler.setControllerMap(controllers);
		StageHandler.initialize();
		
		//show menu
		StageHandler.backToMenu();
		
		primaryStage.setAlwaysOnTop(false);
		primaryStage.show();
		audioEngine.playBackground(SFX.BACKGROUND.MENU, true);
	}
	
	private void loadCharSelection(Map<String, Scene> sceneMap, Map<String, Controller> controllers) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("view/menu/characterSelect.fxml"));
		Scene s = new Scene(loader.load());
		sceneMap.put("character_selection", s);
		
		controllers.put("character_selection", loader.getController());
		controllers.get("character_selection").init();
	}
	
	private void loadCommands(Map<String, Scene> sceneMap, Map<String, Controller> controllers) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("view/menu/options/commands.fxml"));
		Scene s = new Scene(loader.load());
		sceneMap.put("commands", s);
		
		controllers.put("commands", loader.getController());
		controllers.get("commands").init();
	}
	
	private void loadLeaderboard(Map<String, Scene> sceneMap, Map<String, Controller> controllers) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("view/menu/leaderboard.fxml"));
		Scene s = new Scene(loader.load());
		sceneMap.put(SCENES.LEADERBOARD, s);
		
		controllers.put(SCENES.LEADERBOARD, loader.getController());
		controllers.get(SCENES.LEADERBOARD).init();
	}
	
	private void loadMenu(String name, Map<String, Scene> sceneMap, Map<String, Controller> controllers) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("view/menu/" + name + ".fxml"));
		Scene s = new Scene(loader.load());
		sceneMap.put(name, s);
		
		controllers.put(name, loader.getController());
		controllers.get(name).init();
	}
	
	private void loadOptions(String name, Map<String, Scene> sceneMap, Map<String, Controller> controllers) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("view/menu/options/" + name + ".fxml"));
		Scene s = new Scene(loader.load());
		sceneMap.put(name, s);
		controllers.put(name, loader.getController());
		controllers.get(name).init();
	}
	
	private void loadGame(Map<String, Scene> sceneMap) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("view/game.fxml"));
		Scene s = new Scene(loader.load());
		sceneMap.put("gameplay", s);
		gameController = loader.getController();
		GAME = new GameLoop(gameController.getContext());
	}
	
	private void loadRoomGen() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("view/room_engine/room_creation.fxml"));
		Scene s = new Scene(loader.load());
		roomController = loader.getController();
		
		StageHandler.setGenerationEngine(s, roomController);
	}
	
	public static void main(String[] args) {
		audioEngine = new AudioEngine();
		launch(args);
	}
}