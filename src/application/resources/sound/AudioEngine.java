package application.resources.sound;

import application.settings.AppSettings;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;

public class AudioEngine {
	private final Map<String, MediaPlayer> music;
	private final Map<String, AudioClip> effects;
	private MediaPlayer backgroundMusic;
	
	public AudioEngine() {
		music = new HashMap<>();
		effects = new HashMap<>();
		
		loadClip(SFX.EFFECTS.UNLOCK, SFX.EFFECTS.DEAD, SFX.EFFECTS.SHOT, SFX.EFFECTS.HIT, SFX.EFFECTS.STAGE_CLEAR);
		loadClip(SFX.EFFECTS.BACK, SFX.EFFECTS.SELECTED, SFX.EFFECTS.FOCUSED, SFX.EFFECTS.HURT);
		
		loadMedia(SFX.BACKGROUND.MENU, SFX.BACKGROUND.LEADERBOARD, SFX.BACKGROUND.CREATION);
		loadMedia(SFX.GAME.EASY_BATTLE, SFX.GAME.NORMAL_BATTLE, SFX.GAME.HARD_BATTLE);
	}
	
	private void loadClip(String... paths) {
		for (String path: paths) {
//			System.out.println("loading: " + path);
			String clip = getClass().getResource(path).toString();
			effects.put(path, new AudioClip(clip));
			effects.get(path).setVolume(AppSettings.getVOLUME());
		}
	}
	
	private void loadMedia(String... paths) {
		for (String path: paths) {
//			System.out.println("loading: " + path);
			String file = getClass().getResource(path).toString();
			music.put(path, new MediaPlayer(new Media(file)));
			music.get(path).setVolume(AppSettings.getVOLUME());
		}
	}
	
	public void playEffect(String effect) {
		if ( effects.containsKey(effect) ) {
			effects.get(effect).play(AppSettings.getVOLUME());
		}
	}
	
	public void playBackground(String track, boolean repeat) {
		stop();
		if ( music.containsKey(track) ) {
			backgroundMusic = music.get(track);
			if ( repeat ) {
				backgroundMusic.setCycleCount(Integer.MAX_VALUE);
			}
			resume();
		}
	}
	
	public void stop() {
		if ( backgroundMusic != null ) {
			backgroundMusic.stop();
			backgroundMusic = null;
		}
	}
	
	public void resume() {
		if ( backgroundMusic != null ) {
			backgroundMusic.setVolume(AppSettings.getVOLUME());
			backgroundMusic.play();
		}
	}
}
