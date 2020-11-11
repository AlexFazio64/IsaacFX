package application.controller;

import application.model.network.Server;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

public class ServerGUIController {
	@FXML
	public Label statusLbl;
	@FXML
	public Button startBtn;
	@FXML
	public TextArea logger;
	
	@FXML
	public void startServer(ActionEvent actionEvent) {
		Thread t = new Thread(new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				Server.START(logger);
				return null;
			}
		});
		
		t.setDaemon(true);
		t.start();
		
		startBtn.setDisable(true);
		statusLbl.setText("RUNNING");
		statusLbl.setTextFill(Color.web("#00FF00"));
	}
	
}
