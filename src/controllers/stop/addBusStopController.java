package controllers.stop;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import managers.AlertManager;
import managers.CityMapManager;
import models.BusStop;

import java.sql.SQLException;

import db.dao.BusStopDao;
import db.dao.impl.BusStopDaoPG;
import exceptions.AddFailException;
import exceptions.DBConnectionException;
import javafx.event.ActionEvent;

public class addBusStopController {
	@FXML
	private TextField stopNumberField;
	@FXML
	private TextField stopStreetNameField;
	@FXML
	private TextField stopStreetNumberField;
	@FXML
	private Button addStop;

	@FXML
	public void addNewStop(ActionEvent event) {
		BusStop busStop = new BusStop();
		try {
			busStop.setStopNumber(Integer.parseInt(stopNumberField.getText().trim()));
			busStop.setStopStreetName(stopStreetNameField.getText().trim());
			busStop.setStopStreetNumber(Integer.parseInt(stopStreetNumberField.getText().trim()));
		}catch(NumberFormatException e) {
			AlertManager.createAlert(AlertType.ERROR, "Error", "Ingrese números para el número de parada y el número de calle de la misma.");
			return;
		}
		BusStopDao stopDao = new BusStopDaoPG();
		try {
			stopDao.addData(busStop);
			CityMapManager cityMapManager = CityMapManager.getInstance();
			cityMapManager.addStopMap(busStop);
		} catch(AddFailException|DBConnectionException e) {
			AlertManager.createAlert(AlertType.ERROR, "Error", e.getMessage());
		    return;
		}
		AlertManager.createAlert(AlertType.INFORMATION, "Exito", "Se añadió a la persona correctamente");
	    ((Stage) (addStop.getScene().getWindow())).close();
	}
}
