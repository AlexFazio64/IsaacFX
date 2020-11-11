package application.controller;

import application.Main;
import application.model.StageHandler;
import application.resources.graphics.Colors;
import application.resources.localization.Eng;
import application.resources.localization.Indexes;
import application.resources.localization.Ita;
import application.resources.sound.SFX;
import application.settings.AppSettings;
import application.settings.KEYMAP;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class CommandsController implements Controller {
	private boolean focusedOption;
	private int key_id;
	private Button[] keys;
	private Label[] commands;
	private Font font;
	private Font small;
	
	@FXML
	public BorderPane mainpane;
	@FXML
	private Label commLbl;
	@FXML
	public GridPane grid;
	@FXML
	public Label backLbl;
	
	@Override
	public void init() {
		font = new Font("Arial Bold", 32 * AppSettings.getSCALE());
		small = new Font("Arial Bold", 20 * AppSettings.getSCALE());
		focusedOption = false;
		key_id = -1;
		
		grid.getChildren().clear();
		addButtons();
		
		localize();
		refreshSettings();
	}
	
	private void addButtons() {
		commands = new Label[Eng.comm.length];
		keys = new Button[Eng.comm.length];
		
		int id = 0;
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 4; ++col, id++) {
				BorderPane pane = new BorderPane();
				BorderPane lab;
				
				Label name = new Label();
				name.setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
				name.setTextAlignment(TextAlignment.CENTER);
				commands[id] = name;
				lab = new BorderPane(name);
				
				Button change = new Button();
				int localId = id;
				
				change.setTextAlignment(TextAlignment.CENTER);
				change.setText(KEYMAP.all_keys[id].toString());
				change.setWrapText(true);
				change.setFont(small);
				
				change.setBackground(new Background(new BackgroundFill(Color.web(Colors.OPTION_SELECTED), null, null)));
				change.setTextFill(Color.web(Colors.OPTION_AVAILABLE));
				
				change.setFocusTraversable(true);
				
				change.setOnMouseClicked(e -> {
					Main.audioEngine.playEffect(SFX.EFFECTS.SELECTED);
					if ( localId == key_id ) {
						for (Button key: keys) {
							key.setDisable(false);
							change.setBackground(new Background(new BackgroundFill(Color.web(Colors.OPTION_SELECTED), null, null)));
							change.setTextFill(Color.web(Colors.OPTION_AVAILABLE));
							backLbl.requestFocus();
						}
						key_id = -1;
					} else {
						key_id = localId;
						for (int j = 0; j < keys.length; j++)
							if ( key_id != j ) {
								change.setBackground(new Background(new BackgroundFill(Color.web(Colors.OPTION_AVAILABLE), null, null)));
								change.setTextFill(Color.web(Colors.OPTION_SELECTED));
								keys[j].setDisable(true);
							}
					}
				});
				change.setOnKeyPressed(this::changeFocus);
				keys[id] = change;
				
				pane.setCenter(change);
				pane.setBottom(lab);
				
				grid.add(pane, col, row);
			}
		}
	}
	
	@Override
	public void refreshSettings() {
		String image = "application/resources/graphics/splash/blurred.jpg";
		font = new Font("Arial Bold", 32 * AppSettings.getSCALE());
		small = new Font("Arial Bold", 20 * AppSettings.getSCALE());
		BackgroundImage back = new BackgroundImage(new Image(image, AppSettings.getWidth(), AppSettings.getHeight(), true, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		mainpane.setBackground(new Background(back));
		
		commLbl.setFont(font);
		backLbl.setFont(font);
		
		for (Label l: commands) {
			l.setFont(small);
			l.setTextFill(Color.web(Colors.OPTION_SELECTED));
		}
		
		commLbl.setTextFill(Color.web(Colors.OPTION_SELECTED));
		backLbl.setTextFill(Color.web(Colors.OPTION_AVAILABLE));
		backLbl.requestFocus();
	}
	
	@Override
	public void localize() {
		if ( AppSettings.getLANGUAGE().equals("ita") ) {
			commLbl.setText(Ita.lang[Indexes.COMMANDS]);
			backLbl.setText(Ita.lang[Indexes.COMMANDS + 1]);
			for (int i = 0; i < commands.length; i++)
				commands[i].setText(Ita.comm[i]);
		} else if ( AppSettings.getLANGUAGE().equals("eng") ) {
			commLbl.setText(Eng.lang[Indexes.COMMANDS]);
			backLbl.setText(Eng.lang[Indexes.COMMANDS + 1]);
			for (int i = 0; i < commands.length; i++)
				commands[i].setText(Eng.comm[i]);
		}
	}
	
	@Override
	public void select() {
		if ( focusedOption ) {
			backOption();
		}
	}
	
	@Override
	public void setFocus() {
		backLbl.setTextFill(Color.web(focusedOption ? Colors.OPTION_SELECTED : Colors.OPTION_AVAILABLE));
	}
	
	@FXML
	public void changeFocus(KeyEvent e) {
		KeyCode code = e.getCode();
		
		if ( key_id != -1 ) {
			keys[key_id].setText(code.toString());
			KEYMAP.refresh(key_id, code);
			AppSettings.saveSettings();
			return;
		}
		
		if ( code == KEYMAP.MENU_BACK ) {
			Main.audioEngine.playEffect(SFX.EFFECTS.BACK);
			backOption();
		} else if ( code == KEYMAP.MENU_UP || code == KEYMAP.MENU_DOWN ) {
			Main.audioEngine.playEffect(SFX.EFFECTS.FOCUSED);
			focusedOption = true;
			setFocus();
		} else if ( code == KEYMAP.MENU_SELECT ) {
			Main.audioEngine.playEffect(SFX.EFFECTS.SELECTED);
			select();
		}
	}
	
	@Override
	public void loseFocus(MouseEvent mouseEvent) {
		focusedOption = false;
		setFocus();
	}
	
	@Override
	public void selectOption(MouseEvent mouseEvent) {
		Main.audioEngine.playEffect(SFX.EFFECTS.SELECTED);
		select();
	}
	
	@FXML
	private void backOption() {
		AppSettings.saveSettings();
		StageHandler.backToOptions();
	}
	
	public void focusBack(MouseEvent mouseEvent) {
		Main.audioEngine.playEffect(SFX.EFFECTS.FOCUSED);
		focusedOption = true;
		setFocus();
	}
}
