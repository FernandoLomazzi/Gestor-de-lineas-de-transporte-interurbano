package controllers.line;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import db.dao.BusLineDao;
import db.dao.impl.BusLineDaoPG;
import exceptions.DBConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import managers.StageManager;
import models.busline.BusLine;
import models.busline.CheapLine;
import models.busline.PremiumLine;

public class showLineController implements Initializable {
	static public enum avalibleOperations {
		MODIFY,
		DELETE
	}
    @FXML
    private TableView<BusLine> lineTable;
    @FXML
    private TableColumn<BusLine, Color> lineNameColumn;
	@FXML
    private TableColumn<BusLine, String> lineColorColumn;
    @FXML
    private TableColumn<BusLine, String> lineTypeColumn;

	private ObservableList<BusLine> lineRow;
	private avalibleOperations operation;
	
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	lineTable.setRowFactory( tv -> {
    		TableRow<BusLine> row = new TableRow<>();
    		row.setOnMouseClicked(event -> {
    			if (event.getClickCount() == 2 && !row.isEmpty()) {
    				Stage stage = new Stage();
    				stage.initModality(Modality.APPLICATION_MODAL);
    				try {
    					if (operation == avalibleOperations.MODIFY) {
    						System.out.println("Funciono");
							BusLine busLine = row.getItem();
							FXMLLoader loader = null;
							Parent root = null;
							if (busLine.getType() == "Económica") {
								loader = new FXMLLoader(getClass().getResource("/views/line/modLineCheap.fxml"));
								root = loader.load();
								System.out.println(loader);
								modLineCheapController controller = loader.getController();
								System.out.println(controller);
								controller.setCheapLine((CheapLine)busLine);
							}
							if (busLine.getType() == "Superior") {
								loader = new FXMLLoader(getClass().getResource("/views/line/modLinePremium.fxml"));
								root = loader.load();
								modLinePremiumController controller = loader.getController();
								controller.setBusLine((PremiumLine)busLine);
							}
							Scene scene = new Scene(root);
							stage.setTitle("Modificar " + busLine.getName());
							stage.setScene(scene);							
							stage.showAndWait();
    					}
    					else if (operation == avalibleOperations.DELETE) {
    						//Falta
    					}
    				}
    				catch(IOException e) {
    					e.printStackTrace();
    				}
    			}
    		});
    		return row;
    	});
    	BusLineDao busLineDao = new BusLineDaoPG();
		try {
			lineRow = FXCollections.observableList(busLineDao.getAllBusLines());
		} catch (DBConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
    public void setOperation(avalibleOperations operation) {
    	this.operation = operation;
    }
}
