package application.controller;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public interface Controller {
	void init();
	
	void refreshSettings();
	
	void localize();
	
	void select();
	
	void setFocus();
	
	void changeFocus(KeyEvent e);
	
	void loseFocus(MouseEvent mouseEvent);
	
	void selectOption(MouseEvent mouseEvent);
}
