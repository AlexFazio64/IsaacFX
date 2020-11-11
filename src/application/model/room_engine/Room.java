package application.model.room_engine;

import java.util.ArrayList;

public class Room {
	public enum RoomType {START, END, ENEMY}
	
	public static final ArrayList<String> up_openings = new ArrayList<>();
	public static final ArrayList<String> down_openings = new ArrayList<>();
	public static final ArrayList<String> left_openings = new ArrayList<>();
	public static final ArrayList<String> right_openings = new ArrayList<>();
	
	private final int row;
	private final int col;
	private RoomType type;
	private String id;
	
	public Room(int _row, int _col) {
		this.id = "0000";
		this.row = _row;
		this.col = _col;
		type = null;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public String getId() {
		return id;
	}
	
	public RoomType getType() {
		return type;
	}
	
	public void setType(RoomType type) {
		this.type = type;
	}
	
	public void setId(String id) {
		int t = 0;
		
		if ( id.charAt(0) == '1' ) {
			++t;
		}
		if ( id.charAt(1) == '1' ) {
			++t;
		}
		if ( id.charAt(2) == '1' ) {
			++t;
		}
		if ( id.charAt(3) == '1' ) {
			++t;
		}
		
		switch (t) {
			case 1:
				setType(RoomType.END);
				break;
			case 2:
			case 3:
				setType(RoomType.ENEMY);
				break;
			case 4:
				setType(RoomType.START);
				break;
		}
		
		this.id = id;
	}
	
	public boolean canGoUp() {
		return id.charAt(0) == '1';
	}
	
	public boolean canGoDown() {
		return id.charAt(2) == '1';
	}
	
	public boolean canGoRight() {
		return id.charAt(1) == '1';
	}
	
	public boolean canGoLeft() {
		return id.charAt(3) == '1';
	}
	
	private static void putInArray(String name) {
		if ( name.charAt(0) == '1' ) {
			up_openings.add(name);
		}
		if ( name.charAt(1) == '1' ) {
			right_openings.add(name);
		}
		if ( name.charAt(2) == '1' ) {
			down_openings.add(name);
		}
		if ( name.charAt(3) == '1' ) {
			left_openings.add(name);
		}
	}
	
	public static void init() {
		for (int i = 0; i < 16; i++)
			putInArray(Seed.decToBin(i));
	}
}
