package application.model.room_engine;

public class Difficulty {
	private int start;
	private int enemy;
	
	public Difficulty(int enemy) {
		this.start = 1;
		this.enemy = (int) ( enemy * .4 );
	}
	
	public boolean getStart() {
		return start > 0;
	}
	
	public boolean getEnemy() {
		return enemy > 0;
	}
	
	public boolean getEnd() {
		return ( start == 0 ) && ( enemy == 0 );
	}
	
	public void generaStart() {
		this.start--;
	}
	
	public void generaEnemy() {
		this.enemy--;
	}
}