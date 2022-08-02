package controllers.route;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import managers.AlertManager;
import managers.CityMapManager;
import models.BusStop;
import models.Route;
import models.Route.distanceUnits;
import models.utils.SelectTwoStop;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import db.dao.RouteDao;
import db.dao.impl.RouteDaoPG;
import exceptions.AddFailException;
import exceptions.DBConnectionException;
import javafx.event.ActionEvent;

import javafx.scene.control.ChoiceBox;

public class addRouteController implements Initializable{
	@FXML
	private TextField sourceStopField;
	@FXML
	private TextField destinationStopField;
	@FXML
	private TextField distanceField;
	@FXML
	private ChoiceBox<distanceUnits> distanceUnitBox;
	@FXML
	private Button addRouteButton;
	
	private BusStop sourceStop;
	private BusStop destinationStop;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		distanceUnitBox.getItems().add(distanceUnits.Kilómetros);
		distanceUnitBox.getItems().add(distanceUnits.Metros);
		distanceUnitBox.getItems().add(distanceUnits.Millas);
		distanceUnitBox.getSelectionModel().selectFirst();
	}
	public void setSourceStop(BusStop busStop) {
		sourceStop = busStop;
		sourceStopField.setText(sourceStop.getStopStreetName()+" "+sourceStop.getStopStreetNumber());
	}
	public void setDestinationStop(BusStop busStop) {
		destinationStop = busStop;
		destinationStopField.setText(destinationStop.getStopStreetName()+" "+destinationStop.getStopStreetNumber());
	}

	// Event Listener on Button[#addRouteButton].onAction
	@FXML
	public void addRoute(ActionEvent event) {
		Double distance;
		try {
			distance = Double.parseDouble(distanceField.getText().trim());
			if(distance<=0)
				throw new NumberFormatException("Ingrese una distancia válida");
		}catch(NumberFormatException|NullPointerException e) {
			AlertManager.createAlert(AlertType.ERROR, "Error", "Ingrese la distancia entre ambas paradas correctamente.").showAndWait();
			return;
		}
		switch(distanceUnitBox.getSelectionModel().getSelectedItem()) {
			case Kilómetros:
				break;
			case Metros:
				distance *= 0.001;
				break;
			case Millas:
				distance *= 1.609344;
				break;
		}
		Route route = new Route(sourceStop,destinationStop,distance);
		RouteDao routeDao = new RouteDaoPG();
		try {
			routeDao.addData(route);
			CityMapManager cityMapManager = CityMapManager.getInstance();
			cityMapManager.addRouteMap(route);
		} catch (AddFailException|DBConnectionException e) {
			AlertManager.createAlert(AlertType.ERROR, "Error", e.getMessage()).showAndWait();
		    return;
		}
		AlertManager.createAlert(AlertType.INFORMATION, "Exito", "Se ha agregado la calle correctamente.").showAndWait();
	    ((Stage) (addRouteButton.getScene().getWindow())).close();
	}

}
