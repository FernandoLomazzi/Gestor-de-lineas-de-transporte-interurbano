package controllers.route;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import managers.MapManager;
import models.BusStop;
import models.Route;
import models.Route.distanceUnits;
import models.utility.SelectTwoStop;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import db.dao.RouteDao;
import db.dao.impl.RouteDaoPG;
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
		Route route = new Route(sourceStop,destinationStop,distance);
		RouteDao routeDao = new RouteDaoPG();
		try {
			routeDao.addData(route);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		MapManager mapManager = MapManager.getInstance();
		mapManager.addRouteMap(route);
	}

}
