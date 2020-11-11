package application.controller;

import application.Main;
import application.model.StageHandler;
import application.model.network.Client;
import application.model.network.Score;
import application.model.room_engine.Seed;
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
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.util.ArrayList;

public class LeaderboardController implements Controller {
	private Font font;
	private ArrayList<Score> scores;
	private ArrayList<Label> options;
	private ArrayList<Pair<Integer, Seed>> challenges;
	private int focusedOption = -1;
	
	@FXML
	private BorderPane mainpane;
	@FXML
	private Label leadLbl;
	@FXML
	private Label backLbl;
	@FXML
	private VBox vbox;
	@FXML
	private Label errorLbl;
	@FXML
	private HBox errorBox;
	
	@Override
	public void init() {
		font = new Font("Arial Bold", 32 * AppSettings.getSCALE());
		scores = new ArrayList<>();
		options = new ArrayList<>();
		options.add(backLbl);
		localize();
		refreshSettings();
	}
	
	public void downloadLeaderboard() {
		scores = new ArrayList<>();
		vbox.getChildren().clear();
		if ( loadFromServer() ) {
			addToLeaderBoard();
		} else {
			vbox.getChildren().add(errorBox);
		}
	}
	
	private void addToLeaderBoard() {
		challenges = new ArrayList<>(scores.size());
		
		for (int i = 0; i < scores.size(); ++i) {
			Score temp = scores.get(i);
			HBox entry = new HBox();
			
			String text = AppSettings.getLANGUAGE().equals("ita") ? Ita.lang[Indexes.LEADERBOARD + 1] : Eng.lang[Indexes.LEADERBOARD + 1];
			
			challenges.add(new Pair<>(temp.getDimension(), temp.getSeed()));
			
			Label usr = new Label(temp.getUsername());
			//SHOW SEED or PLACEHOLDER
//			Label game = new Label(temp.getSeed().toString());
			Label game = new Label(text);
			Label scr = new Label(String.valueOf(temp.getScore()));
			
			usr.setFont(font);
			game.setFont(font);
			scr.setFont(font);
			
			usr.setTextFill(Color.web(Colors.OPTION_SELECTED));
			game.setTextFill(Color.web(Colors.OPTION_AVAILABLE));
			scr.setTextFill(Color.web(Colors.OPTION_SELECTED));
			
			String tt = AppSettings.getLANGUAGE().equals("ita") ? Ita.lang[Indexes.LEADERBOARD + 2] : Eng.lang[Indexes.LEADERBOARD + 2];
			
			game.setTooltip(new Tooltip(tt));
			
			int index = i;
			game.setOnMouseEntered(e -> {
				Main.audioEngine.playEffect(SFX.EFFECTS.FOCUSED);
				focusedOption = ( options.size() - 1 ) - index;
				game.getTooltip().show(game, e.getScreenX() + 10, e.getScreenY() + 10);
				game.setTextFill(Color.web(Colors.OPTION_SELECTED));
			});
			game.setOnMouseExited(e -> {
				focusedOption = -1;
				game.getTooltip().hide();
				game.setTextFill(Color.web(Colors.OPTION_AVAILABLE));
			});
			game.setOnMouseClicked(e -> {
				Main.audioEngine.playEffect(SFX.EFFECTS.SELECTED);
				Main.gameController.init(StageHandler.challengeSeed(challenges.get(index)));
				StageHandler.setScene(SCENES.CHARSEL);
			});
			
			options.add(game);
			
			BorderPane user, seed, score;
			
			user = new BorderPane(usr);
			seed = new BorderPane(game);
			score = new BorderPane(scr);
			
			HBox.setHgrow(user, Priority.ALWAYS);
			HBox.setHgrow(seed, Priority.ALWAYS);
			HBox.setHgrow(score, Priority.ALWAYS);
			
			entry.getChildren().addAll(user, seed, score);
			VBox.setVgrow(entry, Priority.ALWAYS);
			vbox.getChildren().add(entry);
		}
	}
	
	private boolean loadFromServer() {
		Pair<Boolean, ArrayList<Score>> result = Client.recieveScores();
		scores = result.getValue();
		return result.getKey();
	}
	
	@Override
	public void refreshSettings() {
		font = new Font("Arial Bold", 32 * AppSettings.getSCALE());
		BackgroundImage back = new BackgroundImage(new Image(TexturePath.blurred, AppSettings.getWidth(), AppSettings.getHeight(), true, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		mainpane.setBackground(new Background(back));
		
		leadLbl.setFont(font);
		backLbl.setFont(font);
		errorLbl.setFont(font);
		
		leadLbl.setTextFill(Color.web(Colors.OPTION_SELECTED));
		backLbl.setTextFill(Color.web(Colors.OPTION_AVAILABLE));
	}
	
	@Override
	public void localize() {
		if ( AppSettings.getLANGUAGE().equals("ita") ) {
			leadLbl.setText(Ita.lang[Indexes.LEADERBOARD]);
			backLbl.setText(Ita.lang[Indexes.LEADERBOARD + 3]);
			errorLbl.setText(Ita.lang[Indexes.SERVER_ERROR]);
		} else if ( AppSettings.getLANGUAGE().equals("eng") ) {
			leadLbl.setText(Eng.lang[Indexes.LEADERBOARD]);
			backLbl.setText(Eng.lang[Indexes.LEADERBOARD + 3]);
			errorLbl.setText(Eng.lang[Indexes.SERVER_ERROR]);
		}
	}
	
	@Override
	public void select() {
		switch (focusedOption) {
			case -1:
				break;
			case 0:
				backOption();
				break;
			default:
				Main.gameController.init(StageHandler.challengeSeed(challenges.get(focusedOption - 1)));
				StageHandler.setScene(SCENES.CHARSEL);
				break;
		}
	}
	
	@Override
	public void setFocus() {
		for (int i = 0; i < options.size(); ++i)
			options.get(i).setTextFill(Color.web(focusedOption == i ? Colors.OPTION_SELECTED : Colors.OPTION_AVAILABLE));
	}
	
	@FXML
	public void changeFocus(KeyEvent e) {
		KeyCode code = e.getCode();
		if ( code == KEYMAP.MENU_BACK ) {
			Main.audioEngine.playEffect(SFX.EFFECTS.BACK);
			backOption();
		} else if ( code == KEYMAP.MENU_DOWN || code == KEYMAP.MENU_UP ) {
			Main.audioEngine.playEffect(SFX.EFFECTS.FOCUSED);
			if ( code == KEYMAP.MENU_UP ) {
				--focusedOption;
			} else {
				++focusedOption;
			}
			
			if ( focusedOption > options.size() - 1 ) {
				focusedOption = 0;
			} else if ( focusedOption < 0 ) {
				focusedOption = options.size() - 1;
			}
			
			setFocus();
		} else if ( code == KEYMAP.MENU_SELECT ) {
			Main.audioEngine.playEffect(SFX.EFFECTS.SELECTED);
			select();
		}
	}
	
	private void backOption() {
		StageHandler.backToMenu();
	}
	
	@FXML
	public void loseFocus(MouseEvent mouseEvent) {
		focusedOption = -1;
		setFocus();
	}
	
	@FXML
	public void selectOption(MouseEvent mouseEvent) {
		Main.audioEngine.playEffect(SFX.EFFECTS.SELECTED);
		select();
	}
	
	@FXML
	public void focusBack(MouseEvent mouseEvent) {
		Main.audioEngine.playEffect(SFX.EFFECTS.FOCUSED);
		focusedOption = 0;
		setFocus();
	}
}
