package application;

import application.model.network.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerGUI extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("view/serverGUI.fxml"));
		Scene s = new Scene(loader.load());
		primaryStage.setScene(s);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Server GUI");
		primaryStage.setOnCloseRequest(e -> {
			try {
				Server.STOP();
			} catch (IOException ignored) {
			}
		});
		primaryStage.show();
	}
}
