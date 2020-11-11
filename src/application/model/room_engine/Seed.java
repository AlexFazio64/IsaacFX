package application.model.room_engine;

import java.io.Serializable;

public class Seed implements Serializable {
	private StringBuilder result;
	private int size;
	
	@Override
	public String toString() {
		return compress(result);
	}
	
	public Seed(int length) {
		this.size = length;
		this.result = new StringBuilder(length);
		for (int i = 0; i < length; ++i) {
			result.append('0');
		}
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
}