package controllers.stop;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;

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
import managers.MapManager;
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
		System.out.println("NUEVO: "+busStop);
		this.busStop = busStop;
		stopNumberField.setText(busStop.getStopNumber().toString());
		stopStreetField.setText(busStop.getStopStreetName());
		stopStreetNumberField.setText(busStop.getStopStreetNumber().toString());
	}

	// Event Listener on Button[#modStop].onAction
	@FXML
	public void modifyStop(ActionEvent event) {	
		BusStopDao busStopDao = new BusStopDaoPG();
		busStop.setStopNumber(Integer.parseInt(stopNumberField.getText()));
		busStop.setStopStreetName(stopStreetField.getText());
		busStop.setStopStreetNumber(Integer.parseInt(stopStreetNumberField.getText()));
		try {
			busStopDao.modifyData(busStop);
		} catch (ModifyFailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DBConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MapManager mapManager = MapManager.getInstance();
		mapManager.updateMap();
		Stage stage = (Stage) modStopButton.getScene().getWindow();
	    stage.close();
	}
	@FXML
	public void cancel(ActionEvent event) {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
	    stage.close();
	}


}
