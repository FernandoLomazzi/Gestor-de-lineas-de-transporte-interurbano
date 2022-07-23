package controllers.stop;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import managers.MapManager;
import models.BusStop;

import java.sql.SQLException;

import db.dao.BusStopDao;
import db.dao.impl.BusStopDaoPG;
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
			busStop.setStopNumber(Integer.parseInt(stopNumberField.getText()));
			busStop.setStopStreetName(stopStreetNameField.getText());
			busStop.setStopStreetNumber(Integer.parseInt(stopStreetNumberField.getText()));
		}catch(NumberFormatException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
		    alert.setHeaderText(null);
		    alert.setTitle("Error");
		    alert.setContentText("Ingrese los datos correctamente");
		    alert.showAndWait();
			return;
		}
		BusStopDao stopDao = new BusStopDaoPG();
		try {
			stopDao.addData(busStop);
			MapManager mapManager = MapManager.getInstance();
			mapManager.addStopMap(busStop);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
		    alert.setHeaderText(null);
		    alert.setTitle("Exito");
		    alert.setContentText("Se añadió a la persona correctamente");
		    alert.showAndWait();
		}catch(SQLException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
		    alert.setHeaderText(null);
		    alert.setTitle("Error");
		    alert.setContentText("Ocurrió un error al guardar los datos en la db");
		    alert.showAndWait();
		}
	}
}
