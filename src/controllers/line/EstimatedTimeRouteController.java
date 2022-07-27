package controllers.line;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import managers.AlertManager;
import models.BusLineRoute;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

public class EstimatedTimeRouteController implements Initializable{
	@FXML
	private Button addRouteLineButton;
	@FXML
	private TextField hoursField;
	@FXML
	private TextField minutesField;
	@FXML
	private TextField secondsField;

	private BusLineRoute routeLine;
	private Boolean newRoute;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		newRoute=false;
	}
	public void setBusLineRoute(BusLineRoute routeLine) {
		this.routeLine=routeLine;
	}
	public Boolean isNewRoute() {
		return newRoute;
	}
	@FXML
	public void addRouteLine(ActionEvent event) {
		Integer estimatedTimeInSeconds=0;
		try {
			if(!hoursField.getText().trim().isEmpty())
				estimatedTimeInSeconds += 3600*Integer.parseUnsignedInt(hoursField.getText().trim());
			if(!minutesField.getText().trim().isEmpty())
				estimatedTimeInSeconds += 60*Integer.parseUnsignedInt(minutesField.getText().trim());
			if(!secondsField.getText().trim().isEmpty())
				estimatedTimeInSeconds += Integer.parseUnsignedInt(secondsField.getText().trim());
			
		}catch(NumberFormatException e) {
			AlertManager.createAlert(AlertType.ERROR, "ERROR", "ERROR: Ingrese sólo números en al menos un campo.");
			return;
		}
		if(estimatedTimeInSeconds<=0)
			AlertManager.createAlert(AlertType.ERROR, "ERROR", "ERROR: El tiempo estimado no debe ser nulo");
		else {
			routeLine.setEstimatedTime(estimatedTimeInSeconds);
			newRoute=true;
			((Stage) addRouteLineButton.getScene().getWindow()).close();
		}
			
	}


}
