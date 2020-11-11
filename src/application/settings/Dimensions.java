package application.settings;

public abstract class Dimensions {
	public static final double UP_DOOR_Y = 0;
	public static final double LEFT_DOOR_X = 0;
	public static double DOWN_DOOR_Y = AppSettings.getHeight();
	public static double RIGHT_DOOR_X = AppSettings.getWidth();
	public static double VER_DOOR_X = AppSettings.getWidth() / 2;
	public static double HOR_DOOR_Y = AppSettings.getHeight() / 2;
	public static double ISAAC_HEIGHT = 120 / AppSettings.HD * AppSettings.getSCALE();
	public static double ISAAC_WIDTH = 100 / AppSettings.HD * AppSettings.getSCALE();
	public static double DOOR_DIM = 80 * AppSettings.getSCALE();
	public static double MONSTRO_WIDTH = 130 / AppSettings.FHD * AppSettings.getSCALE();
	public static double MONSTRO_HEIGHT = 100 / AppSettings.FHD * AppSettings.getSCALE();
	public static double WORM_WIDTH = 120 / AppSettings.FHD * AppSettings.getSCALE();
	public static double WORM_HEIGHT = 45 / AppSettings.FHD * AppSettings.getSCALE();
	public static double HEART_DIM = 41 / AppSettings.FHD * AppSettings.getSCALE();
	
	public static void reload() {
		DOWN_DOOR_Y = AppSettings.getHeight();
		RIGHT_DOOR_X = AppSettings.getWidth();
		VER_DOOR_X = AppSettings.getWidth() / 2;
		HOR_DOOR_Y = AppSettings.getHeight() / 2;
		ISAAC_HEIGHT = 120 / AppSettings.HD * AppSettings.getSCALE();
		ISAAC_WIDTH = 100 / AppSettings.HD * AppSettings.getSCALE();
		DOOR_DIM = 80 * AppSettings.getSCALE();
		MONSTRO_WIDTH = 130 / AppSettings.FHD * AppSettings.getSCALE();
		MONSTRO_HEIGHT = 100 / AppSettings.FHD * AppSettings.getSCALE();
		WORM_WIDTH = 120 / AppSettings.FHD * AppSettings.getSCALE();
		WORM_HEIGHT = 45 / AppSettings.FHD * AppSettings.getSCALE();
		HEART_DIM = 41 / AppSettings.FHD * AppSettings.getSCALE();
	}
}
