package controllers.incident;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import db.dao.BusStopDao;
import db.dao.IncidentDao;
import db.dao.impl.BusStopDaoPG;
import db.dao.impl.IncidentDaoPG;
import exceptions.DBConnectionException;
import exceptions.ModifyFailException;
import javafx.event.ActionEvent;

import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import managers.AlertManager;
import managers.CityMapManager;
import models.BusStop;
import models.Incident;
import javafx.scene.control.CheckBox;

import javafx.scene.control.DatePicker;

public class modIncidentController implements Initializable {
	@FXML
	private DatePicker beginDatePicker;
	@FXML
	private DatePicker endDatePicker;
	@FXML
	private TextArea descriptionField;
	@FXML
	private CheckBox concludedBox;
	@FXML
	private Button modIncident;

	private Incident incident;
	
	public void setIncident(Incident incident) {
		this.incident = incident;
		beginDatePicker.setValue(incident.getBeginDate());
		if(incident.getEndDate()!=null) 
			endDatePicker.setValue(incident.getEndDate());
		descriptionField.setText(incident.getDescription());
		concludedBox.setSelected(incident.getConcluded());
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
	}
	// Event Listener on Button[#addIncident].onAction
	@FXML
	public void modIncident(ActionEvent event) {
		LocalDate endDate = endDatePicker.getValue();
		String description = descriptionField.getText().trim();
		Boolean concluded = concludedBox.isSelected();
		IncidentDao incidentDao = new IncidentDaoPG();
		incident.setEndDate(endDate);
		incident.setDescription(description);
		incident.setConcluded(concluded);
		try {
			incidentDao.modifyData(incident);
			BusStopDao busStopDao = new BusStopDaoPG();
			BusStop busStop = incident.getBusStopDisabled();
			if(!busStop.isEnabled() && concluded && busStopDao.isEnabled(busStop)) {
				busStop.setEnabled(true);
				busStopDao.modifyData(busStop);
				CityMapManager cityMapManager = CityMapManager.getInstance();
				cityMapManager.enableStyleStop(busStop);
			}
		}catch (ModifyFailException|DBConnectionException e) {
			AlertManager.createAlert(AlertType.ERROR,"Error",e.getMessage());
		    return;
		}
		AlertManager.createAlert(AlertType.INFORMATION,"Exito","Se ha modificado la incidencia correctamente.");
		((Stage) (modIncident.getScene().getWindow())).close();
	}
}
