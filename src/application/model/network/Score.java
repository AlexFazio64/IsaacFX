package application.model.network;

import application.model.room_engine.Seed;

import java.io.Serializable;

public class Score implements Serializable {
	
	private final String username;
	private final String seed;
	private final int score;
	private final int dimension;
	
	@Override
	public String toString() {
		return username + "~" + seed + "~" + score + "~" + dimension;
	}
	
	public Score(String username, String seed, int score, int dim) {
		this.username = username;
		this.seed = seed;
		this.score = score;
		this.dimension = dim;
	}
	
	public String getUsername() {
		return username;
	}
	
	public Seed getSeed() {
		Seed ret = new Seed(seed.length());
		ret.setResult(seed, dimension);
		return ret;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getDimension() {
		return dimension;
	}
}
