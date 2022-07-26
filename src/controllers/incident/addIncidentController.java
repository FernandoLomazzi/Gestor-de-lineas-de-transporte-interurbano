package controllers.incident;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

import db.dao.BusStopDao;
import db.dao.IncidentDao;
import db.dao.impl.BusStopDaoPG;
import db.dao.impl.IncidentDaoPG;
import exceptions.AddFailException;
import exceptions.DBConnectionException;
import exceptions.ModifyFailException;
import javafx.event.ActionEvent;

import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;
import managers.AlertManager;
import managers.CityMapManager;
import models.BusStop;
import models.Incident;
import javafx.scene.control.CheckBox;

import javafx.scene.control.DatePicker;

public class addIncidentController implements Initializable{
	@FXML
	private DatePicker beginDatePicker;
	@FXML
	private DatePicker endDatePicker;
	@FXML
	private TextArea descriptionField;
	@FXML
	private CheckBox concludedBox;
	@FXML
	private Button addIncident;

	private BusStop busStop;
	
	public void setStop(BusStop busStop) {
		this.busStop = busStop;
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		StringConverter<LocalDate> formatter = new StringConverter<LocalDate>() {
			private DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			@Override
		    public String toString(LocalDate object){
		        return object==null ? null : object.format(defaultFormatter);
		    }
		    @Override
		    public LocalDate fromString(String string) {
		        return (string==null || string.isEmpty()) ? null : LocalDate.parse(string, defaultFormatter);
		    }
		};
		beginDatePicker.setConverter(formatter);
		endDatePicker.setConverter(formatter);
		beginDatePicker.setValue(LocalDate.now());
	}
	@FXML
	public void addIncident(ActionEvent event) {
		LocalDate beginDate = beginDatePicker.getValue();
		LocalDate endDate = endDatePicker.getValue();
		String description = descriptionField.getText().trim();
		Boolean concluded = concludedBox.isSelected();		
		IncidentDao incidentDao = new IncidentDaoPG();
		Incident incident = new Incident(busStop,beginDate,endDate,description,concluded);
		try {
			incidentDao.addData(incident);
			if(busStop.isEnabled()) {
				busStop.setEnabled(false);
				BusStopDao busStopDao = new BusStopDaoPG();
				busStopDao.modifyData(busStop);
				CityMapManager cityMapManager = CityMapManager.getInstance();
				cityMapManager.disableStyleStop(busStop);
			}
		} catch (AddFailException|ModifyFailException|DBConnectionException e) {
			AlertManager.createAlert(AlertType.ERROR,"Error",e.getMessage());
		    return;
		}
		AlertManager.createAlert(AlertType.INFORMATION, "Exito", "Se ha registrado la incidencia correctamente.");
		((Stage) (addIncident.getScene().getWindow())).close();
	}
}
