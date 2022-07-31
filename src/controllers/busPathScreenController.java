package controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import exceptions.NoPathException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import managers.AlertManager;
import managers.CityMapManager;
import managers.LinePathManager;
import models.BusStop;
import models.utils.PathProperty;

public class busPathScreenController implements Initializable,returnScene{
	@FXML
	private BorderPane pane; 
	@FXML
	private TextField sourceStopField;
	@FXML
	private TextField destinationStopField;
	@FXML
	private RadioButton fastestButton;
	@FXML
	private RadioButton shortestButton;
	@FXML
	private RadioButton cheapestButton;
	@FXML
	private Button searchPathButton;
	@FXML
	private Label label1;
	@FXML
	private Label label2;
	@FXML
	private Label label3;
	@FXML
	private Button buyButton;
	@FXML
	private ToggleGroup typeOfPathGroup;
	@FXML 
	private Button prevSceneButton;
	
	private LinePathManager pathManager;
	private BusStop sourceStop;
	private BusStop destinationStop;
	private Scene prevScene;
	
	public busPathScreenController() {
		pathManager = new LinePathManager();
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		buyButton.setDisable(true);
		pane.setCenter(pathManager.getMapView());
		fastestButton.setToggleGroup(typeOfPathGroup);
		shortestButton.setToggleGroup(typeOfPathGroup);
		cheapestButton.setToggleGroup(typeOfPathGroup);
		CityMapManager cityManager = CityMapManager.getInstance();
		List<BusStop> busStops = cityManager.getBusStops();
		AutoCompletionBinding<BusStop> bindSource = TextFields.bindAutoCompletion(sourceStopField, busStops);
		bindSource.setOnAutoCompleted(event -> this.sourceStop=event.getCompletion());
		AutoCompletionBinding<BusStop> bindDestination = TextFields.bindAutoCompletion(destinationStopField, busStops);
		bindDestination.setOnAutoCompleted(event -> this.destinationStop=event.getCompletion());		
	}
	public LinePathManager getPathManager() {
		return this.pathManager;
	}
	@FXML
	public void searchPath(ActionEvent event) {
		if(sourceStop==null || destinationStop==null)
			AlertManager.createAlert(AlertType.ERROR, "ERROR", "ERROR: Debe seleccionar una parada de origen y una de fin.");
		else if(sourceStop.equals(destinationStop))
			AlertManager.createAlert(AlertType.ERROR, "ERROR", "ERROR: Debe seleccionar una parada de origen y fin distintas.");
		else {
			buyButton.setDisable(false);
			sourceStopField.setText(sourceStop.toString());
			destinationStopField.setText(destinationStop.toString());
			PathProperty minPathProperty;
			try {
				if(typeOfPathGroup.getSelectedToggle().equals(fastestButton)) {
					minPathProperty = pathManager.fastestPath(sourceStop, destinationStop);					
				}
				else if(typeOfPathGroup.getSelectedToggle().equals(shortestButton)) {
					minPathProperty = pathManager.shortestPath(sourceStop, destinationStop);
				}
				else if(typeOfPathGroup.getSelectedToggle().equals(cheapestButton)) {
					minPathProperty = pathManager.cheapestPath(sourceStop, destinationStop);
				}
				else {
					AlertManager.createAlert(AlertType.ERROR, "ERROR", "ERROR: Debe seleccionar qué tipo de camino desea.");
					return;
				}
			}catch(NoPathException e) {
				AlertManager.createAlert(AlertType.ERROR, "ERROR", e.getMessage());
				return;
			}
			label1.setText("Tiempo estimado: "+minPathProperty.estimatedTimeToString());
			label2.setText("Distancia: "+minPathProperty.distanceToString());
			label3.setText("Costo: "+minPathProperty.costToString());
		}
		
	}
	
	@FXML
	public void buy(ActionEvent event) {
		AlertManager.createAlert(AlertType.INFORMATION,"Compra Exitosa", "Se ha comprado el boleto exitosamente");
	}
	@Override
	public void setPrevScene(Scene scene) {
		this.prevScene = scene;
	}
	@Override
	public void goToPrevScene(ActionEvent event) {
		((Stage) prevSceneButton.getScene().getWindow()).setScene(prevScene);
	}

}
