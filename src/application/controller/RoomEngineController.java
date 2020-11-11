package application.controller;

import application.Main;
import application.model.StageHandler;
import application.model.room_engine.GameRoom;
import application.model.room_engine.MapGenerator;
import application.model.room_engine.Seed;
import application.resources.sound.SFX;
import application.settings.KEYMAP;
import application.settings.SCENES;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.util.Pair;

public class RoomEngineController {
	private int sr, sc, er, ec;
	private static Seed seed;
	private int previous;
	
	@FXML
	private FlowPane pane;
	@FXML
	private TextArea log;
	@FXML
	private Spinner<Integer> dimension;
	@FXML
	private TextField seedField;
	
	public void init() {
		previous = 0;
		dimension.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 8));
		dimension.getValueFactory().setValue(5);
		
		log.clear();
		seedField.clear();
		constructGrid();
	}
	
	@FXML
	private void generateGrid(ActionEvent actionEvent) {
		if ( seedField.getText().isEmpty() ) {
			constructGrid();
		} else {
			constructGrid(seedField.getText());
			seedField.setText(seed.toString());
		}
	}
	
	@FXML
	private void testSeed(ActionEvent actionEvent) {
		StageHandler.hideGenerator();
		Main.gameController.init(seed);
		StageHandler.setScene(SCENES.CHARSEL);
//		System.out.println(seed.toString());
	}
	
	private void constructGrid() {
		GameRoom.resetID();
		pane.getChildren().clear();
		
		int dim = dimension.getValue();
		seed = new Seed(dim * dim, log, seedField);
		
		double width = 1280 / dim;
		double height = 720 / dim;
		
		if ( previous != dim ) {
			GameRoom.init(width, height);
			log.setText("reloaded textures" + System.lineSeparator());
		}
		
		MapGenerator generator = new MapGenerator(dim);
		
		generateMap(dim, width, height, generator);
	}
	
	private void constructGrid(String newSeed) {
		GameRoom.resetID();
		pane.getChildren().clear();
		
		int dim = dimension.getValue();
		
		seed.setResult(newSeed, dim);
		
		double width = 1280 / dim;
		double height = 720 / dim;
		
		if ( previous != dim ) {
			GameRoom.init(width, height);
			log.appendText("reloaded textures" + System.lineSeparator());
		}
		
		MapGenerator generator;
		
		
		generator = new MapGenerator(seed.getMap(dim));
		generateMap(dim, width, height, generator);
		
	}
	
	private void generateMap(int dim, double width, double height, MapGenerator generator) {
		sr = generator.getStartPos()[0];
		sc = generator.getStartPos()[1];
		er = generator.getEndPos()[0];
		ec = generator.getEndPos()[1];
		
		for (int i = 0; i < dim; ++i) {
			for (int j = 0; j < dim; ++j) {
				GameRoom t = new GameRoom(width, height, generator.get(i, j));
				if ( sr == i && sc == j ) {
					t.markStart();
				}
				if ( er == i && ec == j ) {
					t.markEnd();
				}
				
				seed.parseRoom(t);
				pane.getChildren().add(t);
			}
		}
		
		log.appendText(seed.toString() + System.lineSeparator());
		previous = dim;
	}
	
	public static Seed getSeed() {
		return seed;
	}
	
	public Seed randomizeSeedNoGraphics() {
		init();
		GameRoom.resetID();
		int dim = dimension.getValue();
		seed = new Seed(dim * dim, null, null);
		
		MapGenerator generator = new MapGenerator(dim);
		
		sr = generator.getStartPos()[0];
		sc = generator.getStartPos()[1];
		er = generator.getEndPos()[0];
		ec = generator.getEndPos()[1];
		
		for (int i = 0; i < dim; ++i) {
			for (int j = 0; j < dim; ++j) {
				GameRoom t = new GameRoom(0, 0, generator.get(i, j));
				seed.parseRoom(t);
			}
		}

//		System.out.println(seed.toString());
		
		return seed;
	}
	
	public Seed challengeSeedNoGraphics(Pair<Integer, Seed> pair) {
		init();
		GameRoom.resetID();
		int dim = pair.getKey();
		seed = pair.getValue();
		
		MapGenerator generator = new MapGenerator(seed.getMap(dim));
		
		sr = generator.getStartPos()[0];
		sc = generator.getStartPos()[1];
		er = generator.getEndPos()[0];
		ec = generator.getEndPos()[1];

//		System.out.println(seed.toString());
		
		return seed;
	}
	
	public int[] getStartPosition() {
		return new int[]{sr, sc};
	}
	
	public int[] getEndPosition() {
		return new int[]{er, ec};
	}
	
	public int getDimension() {
		return dimension.getValue();
	}
	
	public void input(KeyEvent keyEvent) {
		KeyCode code = keyEvent.getCode();
		if ( code == KEYMAP.MENU_BACK ) {
			StageHandler.hideGenerator();
			Main.audioEngine.playBackground(SFX.BACKGROUND.MENU, true);
		}
	}
}
