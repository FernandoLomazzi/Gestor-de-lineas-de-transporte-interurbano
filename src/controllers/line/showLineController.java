package controllers.line;

import java.awt.Color;
import java.net.URL;
import java.util.ResourceBundle;

import db.dao.BusLineDao;
import db.dao.impl.BusLineDaoPG;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.busline.BusLine;

public class showLineController implements Initializable {
    @FXML
    private TableView<BusLine> lineTable;
    @FXML
    private TableColumn<BusLine, Color> lineNameColumn;
	@FXML
    private TableColumn<BusLine, String> lineColorColumn;
    @FXML
    private TableColumn<BusLine, String> lineTypeColumn;

    private Scene previousScene;
	private ObservableList<BusLine> lineRow;
	
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	BusLineDao busLineDao = new BusLineDaoPG();
		lineRow = FXCollections.observableList(busLineDao.getAllBusLines());
		lineTable.setItems(lineRow);
		lineNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		lineColorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
		lineTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
	}
    
    @FXML
    public void goBack(ActionEvent event) {
		Stage stage = (Stage) lineTable.getScene().getWindow();
		stage.close();
    }

}
