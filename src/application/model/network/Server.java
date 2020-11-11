package application.model.network;

import application.model.room_engine.Seed;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	private static ArrayList<Score> scores;
	private static final String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "scores.isaacscores";
	private static boolean run = true;
	private static ServerSocket serverSocket;
	
	private static void READ() throws IOException {
		scores = new ArrayList<>();
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(path));
			while (bReader.ready()) {
				String line = bReader.readLine();
				String[] data = line.split("~");
				
				String user = data[0];
				int score = Integer.parseInt(data[2]);
				int dim = Integer.parseInt(data[3]);
				Seed seed = new Seed(dim * dim);
				seed.setResult(data[1], dim);
				
				Score entry = new Score(user, seed.toString(), score, dim);
				scores.add(entry);
			}
			bReader.close();
			scores.sort((o1, o2) -> o1.getScore() > o2.getScore() ? -1 : 0);
		} catch (IOException e) {
			BufferedWriter bWrite;
			bWrite = new BufferedWriter(new FileWriter(path));
			bWrite.close();
		}
	}
	
	public static void START(TextArea logger) throws IOException {
		serverSocket = new ServerSocket(5000);
		READ();
		run = true;
		
		while (run) {
			try {
				logger.appendText("waiting for connection" + System.lineSeparator());
				Socket socket = serverSocket.accept();
				logger.appendText("client connected: " + socket.getLocalAddress() + System.lineSeparator());
				
				ObjectOutputStream write = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream objectReader = new ObjectInputStream(socket.getInputStream());
				
				String comm;
				comm = (String) objectReader.readObject();
				
				if ( comm.equals("save") ) {
					logger.appendText("user is saving a score..." + System.lineSeparator());
					Score nuovo = (Score) objectReader.readObject();
					logger.appendText("Score: " + nuovo.toString() + System.lineSeparator());
					scores.add(nuovo);
					scores.sort((o1, o2) -> o1.getScore() > o2.getScore() ? -1 : 0);
					BufferedWriter bWrite;
					bWrite = new BufferedWriter(new FileWriter(path, true));
					bWrite.write(nuovo.toString());
					logger.appendText("score added successfully" + System.lineSeparator());
					bWrite.newLine();
					bWrite.close();
					socket.close();
					logger.appendText("----CONNECTION CLOSED----" + System.lineSeparator());
				} else if ( comm.equals("read") ) {
					logger.appendText("user is reading scores..." + System.lineSeparator());
					int size = Math.min(6, scores.size());
					logger.appendText("Sending " + size + " scores..." + System.lineSeparator());
					write.writeObject(size);
					for (int i = 0; i < size; i++) {
						write.writeObject(scores.get(i));
					}
					socket.close();
					logger.appendText("----CONNECTION CLOSED----" + System.lineSeparator());
				}
			} catch (Exception ignored) {
			}
		}
	}
	
	public static void STOP() throws IOException {
		run = false;
		if ( serverSocket != null ) {
			serverSocket.close();
		}
	}
}

