package controllers.line;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import db.dao.CheapLineDao;
import db.dao.PremiumLineDao;
import db.dao.impl.CheapLineDaoPG;
import db.dao.impl.PremiumLineDaoPG;
import exceptions.DBConnectionException;
import exceptions.DeleteFailException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import managers.AlertManager;
import managers.LineMapManager;
import models.busline.BusLine;
import models.busline.CheapLine;
import models.busline.PremiumLine;
import models.utils.ColorFormatter;

public class showLineController implements Initializable {
	static public enum avalibleOperations {
		MODIFY,
		DELETE
	}
    @FXML
    private TableView<BusLine> lineTable;
    @FXML
    private TableColumn<BusLine, String> lineNameColumn;
	@FXML
    private TableColumn<BusLine, Color> lineColorColumn;
    @FXML
    private TableColumn<BusLine, String> lineTypeColumn;

	private LineMapManager lineMapManager;
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
								controller.setLineMapManager(lineMapManager);
								controller.setCheapLine((CheapLine)busLine);
							}
							if (busLine.getType() == "Superior") {
								loader = new FXMLLoader(getClass().getResource("/views/line/modLinePremium.fxml"));
								root = loader.load();
								modLinePremiumController controller = loader.getController();
								controller.setLineMapManager(lineMapManager);
								controller.setPremiumLine((PremiumLine)busLine);
							}
							Scene scene = new Scene(root);
							stage.setTitle("Modificar " + busLine.getName());
							stage.setScene(scene);							
							stage.showAndWait();
    					}
    					else if (operation == avalibleOperations.DELETE) {
    						BusLine busLine = row.getItem();
    						Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    						alert.setHeaderText(null);
    						alert.setTitle("Eliminación de la linea " + busLine.getName());
    						alert.setContentText("¿Está seguro de que desea eliminar la linea " + busLine.getName() + "? Esta acción no se puede deshacer.");
    						Optional<ButtonType> action = alert.showAndWait();
    						if(action.get() == ButtonType.OK) {
								if(busLine.getType() == "Económica") { 
									CheapLineDao cheapLineDao = new CheapLineDaoPG();
									try {
										cheapLineDao.deleteData((CheapLine)busLine);
									}
									catch (DBConnectionException | DeleteFailException e) {
										AlertManager.createAlert(AlertType.ERROR, "Error", e.getMessage());
										return;
									}
								}
								if(busLine.getType() == "Superior") {
									PremiumLineDao premiumLineDao = new PremiumLineDaoPG();
									try {
										premiumLineDao.deleteData((PremiumLine)busLine);
									}
									catch (DBConnectionException | DeleteFailException e) {
										AlertManager.createAlert(AlertType.ERROR, "Error", e.getMessage());
										return;
									}
								}
    						}
    						lineMapManager.removeLine(busLine);
    						((Stage)lineTable.getScene().getWindow()).close();
    					}
    				}
    				catch(IOException e) {
    					e.printStackTrace();
    				}
    				lineTable.refresh();
    			}
    		});
    		return row;
    	});
    	
    	lineColorColumn.setCellFactory(tc -> {
    		return new TableCell<BusLine, Color>() {
    			protected void updateItem(Color cellValue, boolean empty) {
    				super.updateItem(cellValue, empty);
    				if (cellValue == null || empty) {
    					setText(null);
    					setStyle("");
    				}
    				else {
    					setText(null);
    					setStyle("-fx-background-color: " + ColorFormatter.toHexString(cellValue) + ";");
    				}
    			}
    		};
    	});
	}
	@FXML
    public void goBack(ActionEvent event) {
		Stage stage = (Stage) lineTable.getScene().getWindow();
		stage.close();
    }

    public void setOperation(avalibleOperations operation) {
    	this.operation = operation;
    }
    public void setManager(LineMapManager lineMapManager) {
    	System.out.println(lineMapManager);
    	this.lineMapManager = lineMapManager;
		lineRow = FXCollections.observableList(lineMapManager.getAllBusLines());
		lineTable.setItems(lineRow);
		lineNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		lineColorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
		lineTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    }
}
