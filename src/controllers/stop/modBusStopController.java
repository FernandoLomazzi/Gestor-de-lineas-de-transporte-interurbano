package controllers.stop;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import db.dao.BusStopDao;
import db.dao.impl.BusStopDaoPG;
import exceptions.DBConnectionException;
import exceptions.ModifyFailException;
import javafx.event.ActionEvent;

import javafx.scene.control.CheckBox;

import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import managers.AlertManager;
import managers.CityMapManager;
import models.BusStop;

public class modBusStopController{
	@FXML
	private TextField stopNumberField;
	@FXML
	private TextField stopStreetField;
	@FXML
	private TextField stopStreetNumberField;
	@FXML
	private Button modStopButton;
	@FXML
	private Button cancelButton;
	
	private BusStop busStop;
	
	public void setBusStop(BusStop busStop) {
		this.busStop = busStop;
		stopNumberField.setText(busStop.getStopNumber().toString());
		stopStreetField.setText(busStop.getStopStreetName());
		stopStreetNumberField.setText(busStop.getStopStreetNumber().toString());
	}

	@FXML
	public void modifyStop(ActionEvent event) {	
		BusStopDao busStopDao = new BusStopDaoPG();
		busStop.setStopNumber(Integer.parseUnsignedInt(stopNumberField.getText().trim()));
		busStop.setStopStreetName(stopStreetField.getText().trim());
		busStop.setStopStreetNumber(Integer.parseUnsignedInt(stopStreetNumberField.getText().trim()));
		try {
			busStopDao.modifyData(busStop);
			CityMapManager cityMapManager = CityMapManager.getInstance();
			cityMapManager.updateMapView();
		} catch (ModifyFailException|DBConnectionException e) {
			AlertManager.createAlert(AlertType.ERROR, "ERROR", e.getMessage()).showAndWait();
		    return;
		}
		AlertManager.createAlert(AlertType.INFORMATION, "EXITO", "Se ha modificado la parada de colectivos exitosamente.").showAndWait();
		((Stage) modStopButton.getScene().getWindow()).close();
	}
	@FXML
	public void cancel(ActionEvent event) {
		((Stage) cancelButton.getScene().getWindow()).close();
	}
}
