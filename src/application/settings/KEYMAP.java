package application.settings;

import javafx.scene.input.KeyCode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public abstract class KEYMAP {
	public static KeyCode MENU_UP = KeyCode.UP;
	public static KeyCode MENU_DOWN = KeyCode.DOWN;
	public static KeyCode MENU_SELECT = KeyCode.ENTER;
	public static KeyCode MENU_BACK = KeyCode.ESCAPE;
	
	public static KeyCode GAME_UP = KeyCode.W;
	public static KeyCode GAME_LEFT = KeyCode.A;
	public static KeyCode GAME_DOWN = KeyCode.S;
	public static KeyCode GAME_RIGHT = KeyCode.D;
	
	public static KeyCode GAME_SHOOT_UP = KeyCode.I;
	public static KeyCode GAME_SHOOT_LEFT = KeyCode.J;
	public static KeyCode GAME_SHOOT_DOWN = KeyCode.K;
	public static KeyCode GAME_SHOOT_RIGHT = KeyCode.L;
	
	public static KeyCode[] all_keys;
	
	public static void sysDefault() {
		all_keys = new KeyCode[]{MENU_UP, MENU_DOWN, MENU_SELECT, MENU_BACK, GAME_UP, GAME_LEFT, GAME_DOWN, GAME_RIGHT, GAME_SHOOT_UP, GAME_SHOOT_LEFT, GAME_SHOOT_DOWN, GAME_SHOOT_RIGHT};
	}
	
	public static void refresh(int index, KeyCode newCode) {
		switch (index) {
			case 0:
				MENU_UP = newCode;
				break;
			case 1:
				MENU_DOWN = newCode;
				break;
			case 2:
				MENU_SELECT = newCode;
				break;
			case 3:
				MENU_BACK = newCode;
				break;
			case 4:
				GAME_UP = newCode;
				break;
			case 5:
				GAME_LEFT = newCode;
				break;
			case 6:
				GAME_DOWN = newCode;
				break;
			case 7:
				GAME_RIGHT = newCode;
				break;
			case 8:
				GAME_SHOOT_UP = newCode;
				break;
			case 9:
				GAME_SHOOT_LEFT = newCode;
				break;
			case 10:
				GAME_SHOOT_DOWN = newCode;
				break;
			case 11:
				GAME_SHOOT_RIGHT = newCode;
				break;
			default:
				break;
		}
		all_keys = new KeyCode[]{MENU_UP, MENU_DOWN, MENU_SELECT, MENU_BACK, GAME_UP, GAME_LEFT, GAME_DOWN, GAME_RIGHT, GAME_SHOOT_UP, GAME_SHOOT_LEFT, GAME_SHOOT_DOWN, GAME_SHOOT_RIGHT};
	}
	
	public static void load(KeyCode[] keycodes) {
		if ( keycodes.length < 12 ) {
//			System.out.println("file is corrupt, using system defaults as fallback");
			sysDefault();
			AppSettings.saveSettings();
			return;
		}
		
		for (int i = 0; i < keycodes.length; i++) {
			switch (i) {
				case 0:
					MENU_UP = keycodes[i];
					break;
				case 1:
					MENU_DOWN = keycodes[i];
					break;
				case 2:
					MENU_SELECT = keycodes[i];
					break;
				case 3:
					MENU_BACK = keycodes[i];
					break;
				case 4:
					GAME_UP = keycodes[i];
					break;
				case 5:
					GAME_LEFT = keycodes[i];
					break;
				case 6:
					GAME_DOWN = keycodes[i];
					break;
				case 7:
					GAME_RIGHT = keycodes[i];
					break;
				case 8:
					GAME_SHOOT_UP = keycodes[i];
					break;
				case 9:
					GAME_SHOOT_LEFT = keycodes[i];
					break;
				case 10:
					GAME_SHOOT_DOWN = keycodes[i];
					break;
				case 11:
					GAME_SHOOT_RIGHT = keycodes[i];
					break;
				default:
					break;
			}
		}
		all_keys = new KeyCode[]{MENU_UP, MENU_DOWN, MENU_SELECT, MENU_BACK, GAME_UP, GAME_LEFT, GAME_DOWN, GAME_RIGHT, GAME_SHOOT_UP, GAME_SHOOT_LEFT, GAME_SHOOT_DOWN, GAME_SHOOT_RIGHT};
		AppSettings.saveSettings();
	}
	
	public static void save() {
		String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "user.isaacprefs";
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
			for (KeyCode key: all_keys) {
				bw.newLine();
				bw.write("KEY:" + key.toString());
			}
			bw.close();
		} catch (Exception ignored) {
		}
	}
}
