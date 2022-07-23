package controllers.incident;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import javafx.event.ActionEvent;

import javafx.scene.control.TextArea;
import javafx.util.StringConverter;
import managers.MapManager;
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
			//Error si escribis 12 y  te salis
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
		String description = descriptionField.getText();
		Boolean concluded = concludedBox.isSelected();
		if(concluded && endDate==null) {
			//Tirar error;
		}
		else {
			IncidentDao incidentDao = new IncidentDaoPG();
			//esto capaz crashea
			incident.setEndDate(endDate);
			incident.setDescription(description);
			incident.setConcluded(concluded);
			incidentDao.modifyData(incident);
			BusStopDao busStopDao = new BusStopDaoPG();
			BusStop busStop = incident.getBusStopDisabled();
			if(!busStop.isEnabled() && concluded && busStopDao.isEnabled(busStop)) {
				busStop.setEnabled(true);
				busStopDao.modifyData(busStop);
				MapManager mapManager = MapManager.getInstance();
				mapManager.enableStyleStop(busStop);
			}
		}
	}
}
