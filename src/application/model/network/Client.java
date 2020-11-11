package application.model.network;

import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
	public static boolean save(Score entry) {
		try {
			Socket socket = new Socket("localhost", 5000);
			ObjectOutputStream write = new ObjectOutputStream(socket.getOutputStream());
			
			write.writeObject("save");
			write.writeObject(entry);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static Pair<Boolean, ArrayList<Score>> recieveScores() {
		ArrayList<Score> scores = new ArrayList<>();
		try {
			Socket socket = new Socket("localhost", 5000);
			ObjectOutputStream write = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream objectReader = new ObjectInputStream(socket.getInputStream());
			
			write.writeObject("read");
			
			Integer sizeScore = (Integer) objectReader.readObject();
			for (int i = 0; i < sizeScore; i++) {
				scores.add((Score) objectReader.readObject());
			}
			socket.close();
		} catch (IOException | ClassNotFoundException ioException) {
			return new Pair<>(false, scores);
		}
		return new Pair<>(true, scores);
	}
}
