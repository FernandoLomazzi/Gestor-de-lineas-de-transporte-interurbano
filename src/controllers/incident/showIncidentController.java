package controllers.incident;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import db.dao.IncidentDao;
import db.dao.impl.IncidentDaoPG;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.BusStop;
import models.Incident;
import models.utility.MyHeap;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

public class showIncidentController implements Initializable {
	@FXML
	private TableView incidentTable;
	@FXML
	private TableColumn<Incident,BusStop> stopNumberColumn;
	@FXML
	private TableColumn<Incident,LocalDate> beginDateColumn;
	@FXML
	private TableColumn<Incident,LocalDate> endDateColumn;
	@FXML
	private TableColumn<Incident,String> descriptionColumn;
	@FXML
	private TableColumn<Incident,Boolean> concludedColumn;
	private ObservableList<Incident> incidentRow;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {   
		incidentRow = FXCollections.observableArrayList();
		incidentTable.setItems(incidentRow);
		stopNumberColumn.setCellValueFactory(new PropertyValueFactory<>("busStopDisabled"));
		beginDateColumn.setCellValueFactory(new PropertyValueFactory<>("beginDate"));
		endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		concludedColumn.setCellValueFactory(new PropertyValueFactory<>("concluded"));
		
		IncidentDao incidentDao = new IncidentDaoPG();
		MyHeap<Incident> heap = new MyHeap<>();
		for(Incident incident: incidentDao.getAllIncident()) {
			heap.push(incident);
		}
		while(!heap.empty()) {
			incidentRow.add(heap.top());
			heap.pop();
		}
	}
	
}
