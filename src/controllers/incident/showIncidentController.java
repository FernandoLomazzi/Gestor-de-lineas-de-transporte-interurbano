package controllers.incident;

import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.BusStop;
import models.Incident;
import models.utility.MyHeap;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;

public class showIncidentController implements Initializable {
	@FXML
	private TableView<Incident> incidentTable;
	@FXML
	private TableColumn<Incident,BusStop> stopNumberColumn;
	@FXML
	private TableColumn<Incident,LocalDate> beginDateColumn;
	@FXML
	private TableColumn<Incident,LocalDate> endDateColumn;
	@FXML
	private TableColumn<Incident,String> descriptionColumn;

	private ObservableList<Incident> incidentRow;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		incidentTable.setRowFactory( tv -> {
		    TableRow<Incident> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && !row.isEmpty() ) {
		        	System.out.println(row.getItem());
		    		Stage stage = new Stage();
		    		stage.initModality(Modality.APPLICATION_MODAL);
		    		try {
		    			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/incident/modIncident.fxml"));
		    			Parent root = loader.load();
		    			modIncidentController controller = loader.getController();
		    			controller.setIncident(row.getItem());
		    			Scene scene =  new Scene(root);
		    	        stage.setScene(scene);
		    	        stage.showAndWait();
		    		} catch (IOException e) {
		    			e.printStackTrace();
		    		}
		        }
		    });
		    return row ;
		});
		incidentRow = FXCollections.observableArrayList();
		incidentTable.setItems(incidentRow);
		stopNumberColumn.setCellValueFactory(new PropertyValueFactory<>("busStopDisabled"));
		beginDateColumn.setCellValueFactory(new PropertyValueFactory<>("beginDate"));
		endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		
		IncidentDao incidentDao = new IncidentDaoPG();
		MyHeap<Incident> heap = new MyHeap<>();
		for(Incident incident: incidentDao.getAllInconcludedIncident()) {
			heap.push(incident);
		}
		while(!heap.empty()) {
			incidentRow.add(heap.top());
			heap.pop();
		}
	}
	
}
