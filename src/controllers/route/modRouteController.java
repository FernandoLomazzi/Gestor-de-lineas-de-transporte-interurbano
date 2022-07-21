package controllers.route;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import managers.GraphManager;
import models.BusStop;
import models.Route;
import models.Route.distanceUnits;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import db.dao.RouteDao;
import db.dao.impl.RouteDaoPG;
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
		distanceUnitBox.getItems().add(distanceUnits.Kilómetros);
		distanceUnitBox.getItems().add(distanceUnits.Metros);
		distanceUnitBox.getItems().add(distanceUnits.Millas);
		distanceUnitBox.getSelectionModel().selectFirst();
	}
	public void setRoute(Route oldRoute) {
		this.route = oldRoute;
		sourceStopField.setText(route.getSourceStop().getStopStreetName()+" "+route.getSourceStop().getStopStreetNumber());
		destinationStopField.setText(route.getDestinationStop().getStopStreetName()+" "+route.getDestinationStop().getStopStreetNumber());
	}

	// Event Listener on Button[#addRouteButton].onAction
	@FXML
	public void modRoute(ActionEvent event) {
		Double distance;
		try {
			distance = Double.parseDouble(distanceField.getText());
		}catch(NumberFormatException|NullPointerException e) {
			e.printStackTrace();
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
		RouteDao routeDao = new RouteDaoPG();
		route.setDistanceInKM(distance);
		routeDao.modRouteDistance(route);
		GraphManager graphManager = GraphManager.getInstance();
		graphManager.updateMap();
	}
}
