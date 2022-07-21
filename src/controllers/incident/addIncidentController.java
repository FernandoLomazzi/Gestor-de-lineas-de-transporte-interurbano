package controllers.incident;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

import db.dao.IncidentDao;
import db.dao.impl.IncidentDaoPG;
import javafx.event.ActionEvent;

import javafx.scene.control.TextArea;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;
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
	public void addIncident(ActionEvent event) {
		LocalDate beginDate = beginDatePicker.getValue();
		LocalDate endDate = endDatePicker.getValue();
		String description = descriptionField.getText();
		Boolean concluded = concludedBox.isSelected();
		if(concluded && endDate==null) {
			//Tirar error;
		}
		else {
			IncidentDao incidentDao = new IncidentDaoPG();
			Incident incident = new Incident(busStop,beginDate,endDate,description,concluded);
			try {
				incidentDao.addData(incident);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
