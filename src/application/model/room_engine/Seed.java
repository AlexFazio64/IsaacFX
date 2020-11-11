package application.model.room_engine;

import application.Main;
import application.resources.localization.Eng;
import application.resources.localization.Indexes;
import application.resources.localization.Ita;
import application.settings.AppSettings;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import java.io.Serializable;

public class Seed implements Serializable {
	private final TextField input;
	private final TextArea printer;
	private StringBuilder result;
	private int size;
	
	@Override
	public String toString() {
		return compress(result);
	}
	
	public Seed(int length, TextArea log, TextField seedField) {
		this.size = length;
		this.input = seedField;
		this.printer = log;
		this.result = new StringBuilder(length);
		for (int i = 0; i < length; ++i) {
			result.append('0');
		}
	}
	
	public void printCurrent() {
		printer.appendText(compress(result) + System.lineSeparator());
		input.setText(compress(result));
	}
	
	public static String decToBin(int dec) {
		StringBuilder bin = new StringBuilder();
		
		for (int i = 0; i < 4; ++i) {
			bin.append(dec % 2);
			dec /= 2;
		}
		
		bin.reverse();
		return bin.toString();
	}
	
	public static int binToDec(String bin) {
		int res = 0;
		
		for (int i = 0; i < 4; ++i) {
			if ( bin.charAt(3 - i) == '1' ) {
				res += Math.pow(2, i);
			}
		}
		
		return res;
	}
	
	private static char decToHex(int dec) {
		switch (dec) {
			case 10:
				return 'A';
			case 11:
				return 'B';
			case 12:
				return 'C';
			case 13:
				return 'D';
			case 14:
				return 'E';
			case 15:
				return 'F';
			default:
				return String.valueOf(dec).charAt(0);
		}
	}
	
	public static String hexToRoomID(char hex) {
		switch (hex) {
			case '0':
				return "0000";
			case '1':
				return "0001";
			case '2':
				return "0010";
			case '3':
				return "0011";
			case '4':
				return "0100";
			case '5':
				return "0101";
			case '6':
				return "0110";
			case '7':
				return "0111";
			case '8':
				return "1000";
			case '9':
				return "1001";
			case 'A':
				return "1010";
			case 'B':
				return "1011";
			case 'C':
				return "1100";
			case 'D':
				return "1101";
			case 'E':
				return "1110";
			case 'F':
				return "1111";
			
			default:
				return "";
		}
	}
	
	public void parseRoom(GameRoom t) {
		if ( validated(result.toString()) ) {
			result.setCharAt(t.getLocal_id(), decToHex(t.getRoom_type()));
		} else {
			if ( AppSettings.getLANGUAGE().equals("ita") ) {
				printer.appendText(Ita.lang[Indexes.SEED_ERROR]);
			} else {
				printer.appendText(Eng.lang[Indexes.SEED_ERROR]);
			}
		}
	}
	
	public Room[][] getMap(int dim) {
		Room[][] rooms = new Room[dim][dim];
		
		if ( !validated(result.toString()) ) {
			if ( AppSettings.getLANGUAGE().equals("ita") ) {
				printer.appendText(Ita.lang[Indexes.SEED_ERROR] + System.lineSeparator());
			} else {
				printer.appendText(Eng.lang[Indexes.SEED_ERROR] + System.lineSeparator());
			}
		}
		
		for (int i = 0; i < dim; ++i) {
			for (int j = 0; j < dim; ++j) {
				rooms[i][j] = new Room(i, j);
				if ( ( i * dim + j ) < ( dim * dim - result.length() ) ) {
					rooms[i][j].setId(hexToRoomID('0'));
				} else {
					rooms[i][j].setId(hexToRoomID(result.charAt(i * dim + j)));
				}
			}
		}
		return rooms;
	}
	
	public void setResult(String result, int dim) {
		this.result = new StringBuilder(result);
		size = dim * dim;
		validated(result);
	}
	
	private boolean validated(String test) {
		if ( result.length() < size ) {
			int l = result.length();
			int z = size;
			result = new StringBuilder(z);
			for (int i = 0; i < z - l; i++) {
				result.append('0');
			}
			result.append(test);
		}
		
		return test.matches("\\p{XDigit}+");
	}
	
	private String compress(StringBuilder _res) {
		StringBuilder builder = new StringBuilder(_res);
		while (true) {
			if ( builder.charAt(0) == '0' ) {
				builder.deleteCharAt(0);
			} else {
				return builder.toString();
			}
		}
	}
	
	public Image getRoom(int i, int j) {
		String room_id = hexToRoomID(result.charAt(i * Main.roomController.getDimension() + j));
		return new GameRoom(AppSettings.getWidth(), AppSettings.getHeight(), room_id).getTexture();
	}
}