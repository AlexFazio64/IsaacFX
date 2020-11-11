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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class OptionMenuController implements Controller {
	private int focusedOption = 0;
	private Label[] options;
	
	@FXML
	private Label resLbl;
	@FXML
	private Label volLbl;
	@FXML
	private Label langLbl;
	@FXML
	private Label commLbl;
	@FXML
	private Label backLbl;
	@FXML
	private Slider volSlider;
	@FXML
	private Button itaBtn;
	@FXML
	private Button engBtn;
	@FXML
	private ChoiceBox<String> resCHBX;
	
	@Override
	public void init() {
		options = new Label[]{resLbl, volLbl, langLbl, commLbl, backLbl};
		
		resCHBX.getItems().clear();
		resCHBX.getItems().add("Standard");
		resCHBX.getItems().add("HD");
		resCHBX.getItems().add("FHD");
		
		fetchResolution();
		resCHBX.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> changeResolution(newValue));
		
		fetchVolume();
		volSlider.valueProperty().addListener((observable, oldValue, newValue) -> AppSettings.setVOLUME(newValue.doubleValue()));
		
		fetchLanguage();
		
		localize();
		setFocus();
	}
	
	@Override
	public void select() {
		switch (focusedOption) {
			case 3:
				commOption();
				break;
			case 4:
				backOption();
				break;
		}
	}
	
	@Override
	public void setFocus() {
		for (Label option: options) {
			option.setTextFill(Color.web(Colors.OPTION_SELECTED));
		}
		
		if ( focusedOption == 3 ) {
			options[4].setTextFill(Color.web(Colors.OPTION_AVAILABLE));
		} else {
			options[3].setTextFill(Color.web(Colors.OPTION_AVAILABLE));
		}
	}
	
	@Override
	public void changeFocus(KeyEvent e) {
		KeyCode key = e.getCode();
		if ( key == KEYMAP.MENU_SELECT ) {
			Main.audioEngine.playEffect(SFX.EFFECTS.SELECTED);
			select();
		} else if ( key == KEYMAP.MENU_UP || key == KEYMAP.MENU_DOWN ) {
			Main.audioEngine.playEffect(SFX.EFFECTS.FOCUSED);
			if ( key == KEYMAP.MENU_UP ) {
				--focusedOption;
			} else {
				++focusedOption;
			}
			
			if ( focusedOption > 4 ) {
				focusedOption = 3;
			} else if ( focusedOption < 3 ) {
				focusedOption = 4;
			}
			
			setFocus();
		} else if ( key == KEYMAP.MENU_BACK ) {
			Main.audioEngine.playEffect(SFX.EFFECTS.BACK);
			backOption();
		}
	}
	
	@Override
	public void loseFocus(MouseEvent mouseEvent) {
		for (int i = 3; i < 5; i++) {
			options[i].setTextFill(Color.web(Colors.OPTION_AVAILABLE));
		}
	}
	
	@Override
	public void selectOption(MouseEvent mouseEvent) {
		Main.audioEngine.playEffect(SFX.EFFECTS.SELECTED);
		select();
	}
	
	public void refreshSettings() {
		fetchLanguage();
		fetchResolution();
		fetchVolume();
	}
	
	public void localize() {
		for (int i = 0; i < 5; i++) {
			if ( AppSettings.getLANGUAGE().equals("eng") ) {
				options[i].setText(Eng.lang[i + Indexes.OPTION]);
			} else {
				options[i].setText(Ita.lang[i + Indexes.OPTION]);
			}
		}
	}
	
	private void fetchLanguage() {
		if ( AppSettings.getLANGUAGE().equals("eng") ) {
			engBtn.setVisible(false);
			itaBtn.setVisible(true);
		}
		if ( AppSettings.getLANGUAGE().equals("ita") ) {
			engBtn.setVisible(true);
			itaBtn.setVisible(false);
		}
	}
	
	@FXML
	private void setEngLang(MouseEvent mouseEvent) {
		AppSettings.setLANGUAGE("eng");
		engBtn.setVisible(false);
		itaBtn.setVisible(true);
		StageHandler.localize();
	}
	
	@FXML
	private void setItaLang(MouseEvent mouseEvent) {
		AppSettings.setLANGUAGE("ita");
		itaBtn.setVisible(false);
		engBtn.setVisible(true);
		StageHandler.localize();
	}
	
	private void fetchResolution() {
		if ( AppSettings.getSCALE() == AppSettings.STD ) {
			resCHBX.setValue("Standard");
		} else if ( AppSettings.getSCALE() == AppSettings.HD ) {
			resCHBX.setValue("HD");
		} else if ( AppSettings.getSCALE() == AppSettings.FHD ) {
			resCHBX.setValue("FHD");
		}
	}
	
	private void changeResolution(Object res) {
		switch (res.toString()) {
			case "0":
				AppSettings.setSCALE(AppSettings.STD);
				break;
			case "1":
				AppSettings.setSCALE(AppSettings.HD);
				break;
			case "2":
				AppSettings.setSCALE(AppSettings.FHD);
				break;
		}
		Dimensions.reload();
	}
	
	private void fetchVolume() {
		volSlider.setValue(AppSettings.getVOLUME());
		Main.audioEngine.resume();
	}
	
	@FXML
	private void focusCommands(MouseEvent mouseEvent) {
		Main.audioEngine.playEffect(SFX.EFFECTS.FOCUSED);
		focusedOption = 3;
		setFocus();
	}
	
	@FXML
	private void focusBack(MouseEvent mouseEvent) {
		Main.audioEngine.playEffect(SFX.EFFECTS.FOCUSED);
		focusedOption = 4;
		setFocus();
	}
	
	private void commOption() {
		AppSettings.saveSettings();
		StageHandler.refreshSettings();
		StageHandler.initialize("commands");
		StageHandler.setScene(SCENES.COMMANDS);
	}
	
	private void backOption() {
		AppSettings.saveSettings();
		Dimensions.reload();
		StageHandler.backToMenu();
	}
}
