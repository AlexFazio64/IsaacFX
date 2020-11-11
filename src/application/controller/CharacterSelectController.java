package application.controller;

import application.Main;
import application.model.StageHandler;
import application.model.game_engine.Player;
import application.resources.graphics.Colors;
import application.resources.graphics.TexturePath;
import application.resources.localization.Eng;
import application.resources.localization.Indexes;
import application.resources.localization.Ita;
import application.resources.sound.SFX;
import application.settings.AppSettings;
import application.settings.KEYMAP;
import application.settings.SCENES;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class CharacterSelectController implements Controller {
	private Font font;
	private ArrayList<Label> options;
	private int focusedOption = -1;
	private ImageView[] sprites;
	
	@FXML
	public AnchorPane mainpane;
	@FXML
	public Label charLbl;
	@FXML
	public Label backLbl;
	@FXML
	public Label darkisaacLbl;
	@FXML
	public Label isaacLbl;
	@FXML
	public ImageView isaacSprite;
	@FXML
	public ImageView darkSprite;
	
	@Override
	public void init() {
		options = new ArrayList<>();
		
		isaacSprite.setImage(new Image(TexturePath.isaac + "down/0.png"));
		darkSprite.setImage(new Image(TexturePath.darkisaac + "down/0.png"));
		sprites = new ImageView[]{new ImageView(), isaacSprite, darkSprite};
		
		options.add(backLbl);
		options.add(isaacLbl);
		options.add(darkisaacLbl);
		
		localize();
		refreshSettings();
	}
	
	@Override
	public void refreshSettings() {
		font = new Font("Arial Bold", 32 * AppSettings.getSCALE());
		String image = "application/resources/graphics/splash/character_selection.jpg";
		BackgroundImage back = new BackgroundImage(new Image(image, AppSettings.getWidth(), AppSettings.getHeight(), true, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		mainpane.setBackground(new Background(back));
		
		options.forEach(e -> {
			e.setFont(font);
			e.setTextFill(Color.web(Colors.OPTION_AVAILABLE));
		});
		charLbl.setFont(font);
		charLbl.setTextFill(Color.web(Colors.OPTION_SELECTED));
		mainpane.requestFocus();
	}
	
	@Override
	public void localize() {
		isaacLbl.setText("ISAAC");
		darkisaacLbl.setText("DARK ISAAC");
		charLbl.setText(AppSettings.getLANGUAGE().equals("ita") ? Ita.lang[Indexes.CHARSEL] : Eng.lang[Indexes.CHARSEL]);
		backLbl.setText(AppSettings.getLANGUAGE().equals("ita") ? Ita.lang[Indexes.CHARSEL + 1] : Eng.lang[Indexes.CHARSEL + 1]);
	}
	
	@Override
	public void select() {
		switch (focusedOption) {
			case 0:
				backOption();
				break;
			case 1:
			case 2:
				playWith(focusedOption);
				break;
		}
	}
	
	@Override
	public void setFocus() {
		options.forEach(e -> e.setTextFill(Color.web(Colors.OPTION_AVAILABLE)));
		options.get(focusedOption).setTextFill(Color.web(Colors.OPTION_SELECTED));
		for (int i = 1; i < 3; ++i) {
			sprites[i].setOpacity(.5);
		}
		sprites[focusedOption].setOpacity(1);
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
			
			if ( focusedOption > 2 ) {
				focusedOption = 0;
			} else if ( focusedOption < 0 ) {
				focusedOption = 2;
			}
			
			setFocus();
		} else if ( key == KEYMAP.MENU_BACK ) {
			Main.audioEngine.playEffect(SFX.EFFECTS.BACK);
			backOption();
		}
	}
	
	@Override
	public void loseFocus(MouseEvent mouseEvent) {
		for (int i = 0; i < focusedOption; i++) {
			options.get(i).setTextFill(Color.web(Colors.OPTION_AVAILABLE));
		}
		focusedOption = -1;
		isaacSprite.setOpacity(.5);
		darkSprite.setOpacity(.5);
	}
	
	@Override
	public void selectOption(MouseEvent mouseEvent) {
	}
	
	public void selectIsaac(MouseEvent mouseEvent) {
		select();
	}
	
	public void focusIsaac(MouseEvent mouseEvent) {
		Main.audioEngine.playEffect(SFX.EFFECTS.FOCUSED);
		focusedOption = 1;
		isaacSprite.setOpacity(1);
		darkSprite.setOpacity(.5);
		setFocus();
	}
	
	public void selectDark(MouseEvent mouseEvent) {
		select();
	}
	
	public void focusDark(MouseEvent mouseEvent) {
		Main.audioEngine.playEffect(SFX.EFFECTS.FOCUSED);
		focusedOption = 2;
		darkSprite.setOpacity(1);
		isaacSprite.setOpacity(.5);
		setFocus();
	}
	
	public void selectBack(MouseEvent mouseEvent) {
		select();
	}
	
	public void focusBack(MouseEvent mouseEvent) {
		Main.audioEngine.playEffect(SFX.EFFECTS.FOCUSED);
		focusedOption = 0;
		setFocus();
	}
	
	private void backOption() {
		StageHandler.backToMenu();
	}
	
	private void playWith(int character) {
		StageHandler.setScene(SCENES.GAMEPLAY);
		Main.GAME.play(character == 1 ? Player.PLAYERTYPE.ISAAC : Player.PLAYERTYPE.DARKISAAC);
	}
}
