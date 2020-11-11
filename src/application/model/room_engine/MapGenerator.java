package application.model.room_engine;

import java.util.Random;
import java.util.Stack;

public class MapGenerator {
	private Room[][] map;
	private final Random gen = new Random();
	private int s_row, s_col;
	private int e_row, e_col;
	private Difficulty choice;
	
	public MapGenerator(Room[][] m) {
		this.map = m;
		Room.init();
		
		connectMaze();
		findPositions();
		optimize();
	}
	
	public MapGenerator(int rows) {
		Room.init();
		this.choice = new Difficulty(rows);
		
		this.map = new Room[rows][rows];
		initMap(rows);
		
		randomizeMap(rows);
		
		connectMaze();
		findPositions();
		optimize();
	}
	
	private void randomizeMap(int rows) {
		Stack<Room> da_visitare;
		Stack<Room> visitate;
		int tr = gen.nextInt(rows), tc = gen.nextInt(rows);
		map[tr][tc].setId("1111");
		choice.generaStart();
		
		da_visitare = new Stack<>();
		visitate = new Stack<>();
		
		da_visitare.add(map[tr][tc]);
		
		while (!da_visitare.empty()) {
			Room current = da_visitare.pop();
			visitate.add(current);
			
			if ( current.canGoUp() ) {
				if ( ( current.getRow() - 1 ) >= 0 && !visitate.contains(map[current.getRow() - 1][current.getCol()]) ) {
					if ( choice.getStart() ) {
						up(current.getRow() - 1, current.getCol(), Room.RoomType.START);
						choice.generaStart();
					} else if ( choice.getEnemy() ) {
						up(current.getRow() - 1, current.getCol(), Room.RoomType.ENEMY);
						choice.generaEnemy();
					} else if ( choice.getEnd() ) {
						up(current.getRow() - 1, current.getCol(), Room.RoomType.END);
					}
					da_visitare.add(map[current.getRow() - 1][current.getCol()]);
				}
			}
			
			if ( current.canGoDown() ) {
				if ( ( current.getRow() + 1 ) < rows && !visitate.contains(map[current.getRow() + 1][current.getCol()]) ) {
					if ( choice.getStart() ) {
						down(current.getRow() + 1, current.getCol(), Room.RoomType.START);
						choice.generaStart();
					} else if ( choice.getEnemy() ) {
						down(current.getRow() + 1, current.getCol(), Room.RoomType.ENEMY);
						choice.generaEnemy();
					} else if ( choice.getEnd() ) {
						down(current.getRow() + 1, current.getCol(), Room.RoomType.END);
					}
					da_visitare.add(map[current.getRow() + 1][current.getCol()]);
				}
			}
			
			if ( current.canGoRight() ) {
				if ( ( current.getCol() + 1 ) < rows && !visitate.contains(map[current.getRow()][current.getCol() + 1]) ) {
					if ( choice.getStart() ) {
						right(current.getRow(), current.getCol() + 1, Room.RoomType.START);
						choice.generaStart();
					} else if ( choice.getEnemy() ) {
						right(current.getRow(), current.getCol() + 1, Room.RoomType.ENEMY);
						choice.generaEnemy();
					} else if ( choice.getEnd() ) {
						right(current.getRow(), current.getCol() + 1, Room.RoomType.END);
					}
					da_visitare.add(map[current.getRow()][current.getCol() + 1]);
				}
			}
			
			if ( current.canGoLeft() ) {
				if ( ( current.getCol() - 1 ) >= 0 && !visitate.contains(map[current.getRow()][current.getCol() - 1]) ) {
					if ( choice.getStart() ) {
						left(current.getRow(), current.getCol() - 1, Room.RoomType.START);
						choice.generaStart();
					} else if ( choice.getEnemy() ) {
						left(current.getRow(), current.getCol() - 1, Room.RoomType.ENEMY);
						choice.generaEnemy();
					} else if ( choice.getEnd() ) {
						left(current.getRow(), current.getCol() - 1, Room.RoomType.END);
					}
					da_visitare.add(map[current.getRow()][current.getCol() - 1]);
				}
			}
		}
	}
	
	private void initMap(int rows) {
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < rows; ++j) {
				map[i][j] = new Room(i, j);
			}
		}
	}
	
	private void connectMaze() {
		int rows = map[0].length;
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < rows; j++) {
				if ( map[i][j].getId().equals("0000") ) {
					continue;
				}
				StringBuilder nuovo = new StringBuilder("0000");
				
				//controlla sopra
				if ( ( i - 1 ) >= 0 && map[i - 1][j].canGoDown() ) {
					nuovo.setCharAt(0, '1');
				}
				
				//controlla giu
				if ( ( i + 1 ) < rows && map[i + 1][j].canGoUp() ) {
					nuovo.setCharAt(2, '1');
				}
				
				//controlla destra
				if ( ( j + 1 ) < rows && map[i][j + 1].canGoLeft() ) {
					nuovo.setCharAt(1, '1');
				}
				
				//controlla sinistra
				if ( ( j - 1 ) >= 0 && map[i][j - 1].canGoRight() ) {
					nuovo.setCharAt(3, '1');
				}
				
				map[i][j].setId(nuovo.toString());
			}
		}
	}
	
	private void optimize() {
		int row = map.length;
		boolean right = true, down = true;
		
		while (right) {
			for (int i = 0; i < row; ++i) {
				if ( !map[i][row - 1].getId().equals("0000") ) {
					right = false;
					break;
				}
			}
			
			if ( right ) {
				shiftRight();
			}
		}
		
		while (down) {
			for (int i = 0; i < row; ++i) {
				if ( !map[row - 1][i].getId().equals("0000") ) {
					down = false;
					break;
				}
			}
			
			if ( down ) {
				shiftDown();
			}
		}
	}
	
	private void findPositions() {
		int rows = map.length;
		double dist = 0;
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < rows; j++) {
				if ( map[i][j].getId().equals("0000") ) {
					continue;
				}
				
				for (int k = 0; k < rows; k++) {
					for (int l = 0; l < rows; l++) {
						if ( map[k][l].getId().equals("0000") ) {
							continue;
						}
						
						if ( map[k][l].getType() == Room.RoomType.END ) {
							double d = findDistance(i, j, k, l);
							if ( dist < d ) {
								dist = d;
								s_row = i;
								s_col = j;
								e_row = k;
								e_col = l;
							}
						}
					}
				}
			}
		}
		
		if ( map[s_row][s_col].getId().equals("0000") || map[e_row][e_col].getId().equals("0000") || s_row == e_row && s_col == e_col ) {
			//extremely rare case
			//possibly linked to Difficulty
			//K-pop test passed
			Seed fallback = new Seed(rows * rows, null, null);
			switch (rows) {
				case 3:
					fallback.setResult("471490", 3);
					break;
				case 4:
					fallback.setResult("47100E1008", 4);
					break;
				case 5:
					fallback.setResult("61004F10049", 5);
					break;
				case 6:
					fallback.setResult("4300004F10000C1", 6);
					break;
				case 7:
					fallback.setResult("2000004B000004F100000C1", 7);
					break;
				case 8:
					fallback.setResult("20000004B2000004FD100000C1", 8);
					break;
			}

//			System.out.println("fallback");
			map = fallback.getMap(rows);
			findPositions();
		}
	}
	
	private void shiftDown() {
		++s_row;
		++e_row;
		int row = map.length;
		for (int i = row - 1; i >= 1; --i) {
			for (int j = 0; j < row; ++j) {
				map[i][j] = map[i - 1][j];
			}
		}
		
		for (int i = 0; i < row; ++i) {
			map[0][i] = new Room(0, i);
		}
	}
	
	private void shiftRight() {
		++s_col;
		++e_col;
		int row = map.length;
		for (int i = 0; i < row; ++i) {
			for (int j = row - 1; j >= 1; --j) {
				map[i][j] = map[i][j - 1];
			}
		}
		
		for (int i = 0; i < row; ++i) {
			map[i][0] = new Room(i, 0);
		}
	}
	
	private double findDistance(int s_row, int s_col, int c_r, int c_c) {
		c_r = Math.max(c_r, s_row) - Math.min(c_r, s_row);
		c_c = Math.max(c_c, s_col) - Math.min(c_c, s_col);
		
		return Math.sqrt(Math.pow(c_r, 2) + Math.pow(c_c, 2));
	}
	
	private void up(int row, int col, Room.RoomType type) {
		map[row][col].setId("0000");
		while (map[row][col].getType() != type)
			map[row][col].setId(Room.down_openings.get(gen.nextInt(Room.down_openings.size())));
	}
	
	private void down(int row, int col, Room.RoomType type) {
		map[row][col].setId("0000");
		while (map[row][col].getType() != type)
			map[row][col].setId(Room.up_openings.get(gen.nextInt(Room.up_openings.size())));
	}
	
	private void left(int row, int col, Room.RoomType type) {
		map[row][col].setId("0000");
		while (map[row][col].getType() != type)
			map[row][col].setId(Room.right_openings.get(gen.nextInt(Room.right_openings.size())));
	}
	
	private void right(int row, int col, Room.RoomType type) {
		map[row][col].setId("0000");
		while (map[row][col].getType() != type)
			map[row][col].setId(Room.left_openings.get(gen.nextInt(Room.left_openings.size())));
	}
	
	public String get(int row, int col) {
		return map[row][col].getId();
	}
	
	public int[] getStartPos() {
		return new int[]{s_row, s_col};
	}
	
	public int[] getEndPos() {
		return new int[]{e_row, e_col};
	}
	
}
