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
			busStop.setStopNumber(Integer.parseUnsignedInt(stopNumberField.getText().trim()));
			busStop.setStopStreetName(stopStreetNameField.getText().trim());
			busStop.setStopStreetNumber(Integer.parseUnsignedInt(stopStreetNumberField.getText().trim()));
		}catch(NumberFormatException e) {
			AlertManager.createAlert(AlertType.ERROR, "ERROR", "Debe ingresar números para el número de parada y el número de calle de la misma.").showAndWait();
			return;
		}
		BusStopDao stopDao = new BusStopDaoPG();
		try {
			stopDao.addData(busStop);
			CityMapManager cityMapManager = CityMapManager.getInstance();
			cityMapManager.addStopMap(busStop);
		} catch(AddFailException|DBConnectionException e) {
			AlertManager.createAlert(AlertType.ERROR, "ERROR", e.getMessage()).showAndWait();
		    return;
		}
		AlertManager.createAlert(AlertType.INFORMATION, "EXITO", "Se ha agregado la parada de colectivos exitosamente.").showAndWait();
	    ((Stage) (addStop.getScene().getWindow())).close();
	}
}
