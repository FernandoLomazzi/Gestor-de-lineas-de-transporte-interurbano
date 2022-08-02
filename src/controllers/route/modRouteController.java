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

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import db.dao.RouteDao;
import db.dao.impl.RouteDaoPG;
import exceptions.DBConnectionException;
import exceptions.ModifyFailException;
import javafx.event.ActionEvent;

import javafx.scene.control.ChoiceBox;

public class modRouteController implements Initializable{
	@FXML
	private TextField sourceStopField;
	@FXML
	private TextField destinationStopField;
	@FXML
	private TextField distanceField;
	@FXML
	private ChoiceBox<distanceUnits> distanceUnitBox;
	@FXML
	private Button modRouteButton;
	
	private Route route;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		distanceUnitBox.getItems().add(distanceUnits.Kil�metros);
		distanceUnitBox.getItems().add(distanceUnits.Metros);
		distanceUnitBox.getItems().add(distanceUnits.Millas);
		distanceUnitBox.getSelectionModel().selectFirst();
	}
	public void setRoute(Route oldRoute) {
		this.route = oldRoute;
		sourceStopField.setText(route.getSourceStop().getStopStreetName()+" "+route.getSourceStop().getStopStreetNumber());
		destinationStopField.setText(route.getDestinationStop().getStopStreetName()+" "+route.getDestinationStop().getStopStreetNumber());
	}

	@FXML
	public void modRoute(ActionEvent event) {
		Double distance;
		try {
			distance = Double.parseDouble(distanceField.getText().trim());
			if(distance<=0)
				throw new NumberFormatException();
		}catch(NumberFormatException|NullPointerException e) {
			AlertManager.createAlert(AlertType.ERROR, "ERROR", "Debe ingresar una distancia v�lida entre ambas paradas.").showAndWait();
			return;
		}
		switch(distanceUnitBox.getSelectionModel().getSelectedItem()) {
			case Kil�metros:
				break;
			case Metros:
				distance *= 0.001;
				break;
			case Millas:
				distance *= 1.609344;
				break;
		}
		RouteDao routeDao = new RouteDaoPG();
		route.setDistanceInKM(distance);
		try {
			routeDao.modifyData(route);
			CityMapManager cityMapManager = CityMapManager.getInstance();
			cityMapManager.updateMapView();
		} catch (ModifyFailException|DBConnectionException e) {
			AlertManager.createAlert(AlertType.ERROR, "ERROR", e.getMessage()).showAndWait();
		    return;
		}
		AlertManager.createAlert(AlertType.INFORMATION, "EXITO", "Se ha modificado la calle correctamente.").showAndWait();
	    ((Stage) (modRouteButton.getScene().getWindow())).close();
	}
}
