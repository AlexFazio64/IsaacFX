package application.controller;

import application.Main;
import application.model.StageHandler;
import application.model.room_engine.GameRoom;
import application.model.room_engine.Seed;
import application.resources.graphics.Colors;
import application.resources.localization.Eng;
import application.resources.localization.Indexes;
import application.resources.localization.Ita;
import application.resources.sound.SFX;
import application.settings.AppSettings;
import application.settings.KEYMAP;
import application.settings.SCENES;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class GameController {
	private boolean endgame;
	private BorderPane pane;
	
	@FXML
	public AnchorPane mainpane;
	@FXML
	private Canvas frame_buff;
	
	private Seed current;
	private ArrayList<KeyCode> input;
	
	public void init(Seed _seed) {
		frame_buff.setHeight(AppSettings.getHeight());
		frame_buff.setWidth(AppSettings.getWidth());
		current = _seed;
		GameRoom.resetID();
		GameRoom.init(AppSettings.getWidth(), AppSettings.getHeight());
		input = new ArrayList<>();
		endgame = false;
	}
	
	@FXML
	private void press(KeyEvent keyEvent) {
		KeyCode code = keyEvent.getCode();
		
		if ( !input.contains(code) ) {
			input.add(code);
		}
		
		Main.GAME.getInput(input);
		
		if ( endgame && code == KEYMAP.MENU_SELECT ) {
			StageHandler.backToMenu();
			mainpane.getChildren().remove(pane);
		}
	}
	
	@FXML
	private void release(KeyEvent keyEvent) {
		KeyCode code = keyEvent.getCode();
		input.remove(code);
	}
	
	public GraphicsContext getContext() {
		return frame_buff.getGraphicsContext2D();
	}
	
	public Seed getCurrentSeed() {
		return current;
	}
	
	public void endGame() {
		pane = new BorderPane();
		
		Label scoreLbl = new Label(( AppSettings.getLANGUAGE().equals("ita") ? Ita.lang[Indexes.ENDGAME] : Eng.lang[Indexes.ENDGAME] ) + (int) Main.GAME.score);
		Label infoLbl = new Label(AppSettings.getLANGUAGE().equals("ita") ? Ita.lang[Indexes.ENDGAME + 1] : Eng.lang[Indexes.ENDGAME + 1]);
		
		Font f = new Font("Arial Bold", 48 * AppSettings.getSCALE());
		Font f2 = new Font("Arial Bold", 20 * AppSettings.getSCALE());
		
		scoreLbl.setFont(f);
		infoLbl.setFont(f2);
		
		scoreLbl.setTextFill(Color.web(Colors.OPTION_SELECTED));
		infoLbl.setTextFill(Color.web(Colors.OPTION_SELECTED));
		
		pane.setCenter(scoreLbl);
		pane.setBottom(new BorderPane(infoLbl));
		mainpane.getChildren().add(pane);
		
		AnchorPane.setTopAnchor(pane, 0.0);
		AnchorPane.setBottomAnchor(pane, 0.0);
		AnchorPane.setLeftAnchor(pane, 0.0);
		AnchorPane.setRightAnchor(pane, 0.0);
		endgame = true;
	}
}
