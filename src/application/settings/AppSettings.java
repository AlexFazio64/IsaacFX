package application.settings;

import javafx.scene.input.KeyCode;

import java.io.*;
import java.util.Vector;

public abstract class AppSettings {
	public static final double STD = 1.0;
	public static final double HD = 1.25;
	public static final double FHD = 1.875;
	
	private static final int WIDTH = 1024;
	private static final int HEIGHT = 576;
	
	private static double SCALE = HD;
	private static double VOLUME = 50;
	private static String LANGUAGE = "eng";
	
	public static double getWidth() {
		return WIDTH * SCALE;
	}
	
	public static double getHeight() {
		return HEIGHT * SCALE;
	}
	
	public static String getLANGUAGE() {
		return LANGUAGE;
	}
	
	public static void setLANGUAGE(String LANGUAGE) {
		AppSettings.LANGUAGE = LANGUAGE;
	}
	
	public static double getVOLUME() {
		return VOLUME;
	}
	
	public static void setVOLUME(double VOLUME) {
		AppSettings.VOLUME = VOLUME;
	}
	
	public static double getSCALE() {
		return SCALE;
	}
	
	public static void setSCALE(double scl) {
		SCALE = scl;
	}
	
	public static void loadSettings() {
		String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "user.isaacprefs";
		Vector<String> prefs = new Vector<>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				prefs.add(br.readLine().split(":")[1]);
			}
			br.close();
		} catch (Exception e) {
//			System.out.println("no configs found, using system defaults as fallback");
			KEYMAP.sysDefault();
			saveSettings();
			return;
		}
		
		switch (prefs.elementAt(0)) {
			case "1.25":
				setSCALE(AppSettings.HD);
				break;
			case "1.875":
				setSCALE(AppSettings.FHD);
				break;
			default:
				setSCALE(AppSettings.STD);
				break;
		}
		
		double v = Double.parseDouble(prefs.elementAt(1));
		setVOLUME(v);
		
		if ( "ita".equals(prefs.elementAt(2)) ) {
			setLANGUAGE("ita");
		} else {
			setLANGUAGE("eng");
		}
		
		KeyCode[] keys = new KeyCode[prefs.size() - 3];
		
		for (int i = 3; i < prefs.size(); i++) {
			keys[i - 3] = KeyCode.valueOf(prefs.get(i));
		}
		
		KEYMAP.load(keys);
	}
	
	public static void saveSettings() {
		String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "user.isaacprefs";
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path));
			bw.append("SCALE:").append(String.valueOf(SCALE));
			bw.newLine();
			bw.append("VOL:").append(String.valueOf(VOLUME));
			bw.newLine();
			bw.append("LANG:").append(LANGUAGE);
			bw.close();
			KEYMAP.save();
		} catch (Exception ignored) {
		}
	}
}
