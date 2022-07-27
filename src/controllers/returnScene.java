package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;

public interface returnScene {
	public void setPrevScene(Scene scene);
	@FXML
	public void goToPrevScene(ActionEvent event);
}
