package application.controller;

import application.Main;
import application.model.StageHandler;
import application.resources.graphics.Colors;
import application.resources.localization.Eng;
import application.resources.localization.Indexes;
import application.resources.localization.Ita;
import application.resources.sound.SFX;
import application.settings.AppSettings;
import application.settings.Dimensions;
import application.settings.KEYMAP;
import application.settings.SCENES;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class MenuController implements Controller {
	GameController gameController;
	
	int focusedOption = 0;
	private Label[] options;
	
	@FXML
	private Label startLbl;
	@FXML
	private Label optionsLbl;
	@FXML
	private Label leaderLbl;
	@FXML
	private Label createLbl;
	@FXML
	private Label exitLbl;
	
	
	public void init() {
		gameController = Main.gameController;
		options = new Label[]{startLbl, optionsLbl, leaderLbl, createLbl, exitLbl};
		localize();
		setFocus();
		Dimensions.reload();
	}
	
	public void localize() {
		for (int i = 0; i < options.length; i++) {
			if ( AppSettings.getLANGUAGE().equals("eng") ) {
				options[i].setText(Eng.lang[i + Indexes.MENU]);
			} else {
				options[i].setText(Ita.lang[i + Indexes.MENU]);
			}
		}
	}
	
	public void refreshSettings() {
	}
	
	public void select() {
		switch (focusedOption) {
			case 0:
				startOption();
				break;
			case 1:
				optionOption();
				break;
			case 2:
				leaderOption();
				break;
			case 3:
				createOption();
				break;
			case 4:
				exitOption();
				break;
			default:
				break;
		}
	}
	
	public void changeFocus(KeyEvent e) {
		KeyCode key = e.getCode();
		
		if ( key == KEYMAP.MENU_SELECT ) {
			Main.audioEngine.playEffect(SFX.EFFECTS.SELECTED);
			select();
		} else if ( key == KEYMAP.MENU_UP || e.getCode() == KEYMAP.MENU_DOWN ) {
			Main.audioEngine.playEffect(SFX.EFFECTS.FOCUSED);
			
			if ( key == KEYMAP.MENU_UP ) {
				--focusedOption;
			} else {
				++focusedOption;
			}
			
			if ( focusedOption > 4 ) {
				focusedOption = 0;
			} else if ( focusedOption < 0 ) {
				focusedOption = 4;
			}
			
			setFocus();
		} else if ( key == KEYMAP.MENU_BACK ) {
			exitOption();
		}
	}
	
	public void setFocus() {
		for (Label option: options)
			option.setTextFill(Color.web(Colors.OPTION_AVAILABLE));
		options[focusedOption].setTextFill(Color.web(Colors.OPTION_SELECTED));
	}
	
	public void loseFocus(MouseEvent mouseEvent) {
		for (Label option: options)
			option.setTextFill(Color.web(Colors.OPTION_AVAILABLE));
	}
	
	public void selectOption(MouseEvent mouseEvent) {
		Main.audioEngine.playEffect(SFX.EFFECTS.SELECTED);
		select();
	}
	
	@FXML
	public void focusStart(MouseEvent mouseEvent) {
		Main.audioEngine.playEffect(SFX.EFFECTS.FOCUSED);
		focusedOption = 0;
		setFocus();
	}
	
	private void startOption() {
		gameController.init(StageHandler.getRandomSeed());
		StageHandler.initialize("character_selection");
		StageHandler.setScene(SCENES.CHARSEL);
	}
	
	@FXML
	public void focusOptions(MouseEvent mouseEvent) {
		Main.audioEngine.playEffect(SFX.EFFECTS.FOCUSED);
		focusedOption = 1;
		setFocus();
	}
	
	private void optionOption() {
		StageHandler.backToOptions();
	}
	
	
	@FXML
	public void focusLeader(MouseEvent mouseEvent) {
		Main.audioEngine.playEffect(SFX.EFFECTS.FOCUSED);
		focusedOption = 2;
		setFocus();
	}
	
	private void leaderOption() {
		Main.audioEngine.playBackground(SFX.BACKGROUND.LEADERBOARD, true);
		StageHandler.readFromServer();
		StageHandler.setScene(SCENES.LEADERBOARD);
	}
	
	
	@FXML
	private void focusCreate(MouseEvent mouseEvent) {
		Main.audioEngine.playEffect(SFX.EFFECTS.FOCUSED);
		focusedOption = 3;
		setFocus();
	}
	
	private void createOption() {
		StageHandler.showGenerator();
	}
	
	
	@FXML
	private void focusExit(MouseEvent mouseEvent) {
		Main.audioEngine.playEffect(SFX.EFFECTS.FOCUSED);
		focusedOption = 4;
		setFocus();
	}
	
	private void exitOption() {
		Main.audioEngine.stop();
		System.exit(0);
	}
	
}
