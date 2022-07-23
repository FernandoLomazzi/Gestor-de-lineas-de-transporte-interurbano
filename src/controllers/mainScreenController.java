package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import src.com.brunomnsilva.smartgraph.graph.Digraph;
import src.com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import src.com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;

import application.Main;
import controllers.incident.addIncidentController;
import controllers.route.addRouteController;
import controllers.route.modRouteController;
import controllers.stop.modBusStopController;
import db.dao.BusStopDao;
import db.dao.RouteDao;
import db.dao.impl.BusStopDaoPG;
import db.dao.impl.RouteDaoPG;
import exceptions.DBConnectionException;
import exceptions.DeleteFailException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import managers.MapManager;
import models.BusStop;
import models.Route;
import models.utils.SelectTwoStop;

public class mainScreenController implements Initializable{
	@FXML private BorderPane borderPane;
	@FXML private Button addStopButton;
	@FXML private ToggleGroup stopGroup,routeGroup;
	@FXML private ToggleButton modStopButton;
	@FXML private ToggleButton delStopButton;
	@FXML private ToggleButton addRouteButton;
	@FXML private ToggleButton modRouteButton;
	@FXML private ToggleButton delRouteButton;
	@FXML private ToggleButton addIncidentButton;
	@FXML private Button showIncidentButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Reveer
		modStopButton.setToggleGroup(stopGroup);
		delStopButton.setToggleGroup(stopGroup);
		addRouteButton.setToggleGroup(stopGroup);
		modRouteButton.setToggleGroup(stopGroup);
		delRouteButton.setToggleGroup(stopGroup);
		addIncidentButton.setToggleGroup(stopGroup);
	}
	// Event Listener on Button[#addStopButton].onAction
	@FXML
	public void addStop(ActionEvent event) {		
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/views/stop/addBusStop.fxml"));
			Scene scene =  new Scene(root);
	        stage.setScene(scene);
	        stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// Event Listener on Button[#modStopButton].onAction
	@FXML
	public void modStop(ActionEvent event) {
		MapManager mapManager = MapManager.getInstance();
		if(modStopButton.isSelected()) {
			//borderPane.getScene().setCursor(Cursor.HAND);
			mapManager.setVertexDoubleClickAction((SmartGraphVertex<BusStop> v) -> {
				Stage stage = new Stage();
				stage.initModality(Modality.APPLICATION_MODAL);
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/stop/modBusStop.fxml"));
					Parent root = loader.load();
			        modBusStopController controller = loader.getController();
			        controller.setBusStop(v.getUnderlyingVertex().element());
					Scene scene =  new Scene(root);
			        stage.setScene(scene);
			        stage.showAndWait();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		else{
			//borderPane.getScene().setCursor(Cursor.DEFAULT);
			mapManager.setVertexDoubleClickAction(null);
		}
	}
	// Event Listener on Button[#delStopButton].onAction
	@FXML
	public void delStop(ActionEvent event) {
		MapManager mapManager = MapManager.getInstance();
		if(delStopButton.isSelected()) {
			mapManager.setVertexDoubleClickAction((SmartGraphVertex<BusStop> v) -> {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			    alert.setHeaderText(null);
			    alert.setTitle("Eliminación de Parada de Colectivo");
			    alert.setContentText("Desea eliminar la parada de colectivo "+v.getUnderlyingVertex().element()+"?");
			    Optional<ButtonType> action = alert.showAndWait();
			    if (action.get() == ButtonType.OK) {
			    	BusStopDao busStopDao = new BusStopDaoPG();
			    	try {
						busStopDao.deleteData(v.getUnderlyingVertex().element());
				    	mapManager.deleteStopMap(v.getUnderlyingVertex());
					} catch (DeleteFailException|DBConnectionException e) {
						Alert alert2 = new Alert(Alert.AlertType.ERROR);
					    alert2.setHeaderText(null);
					    alert2.setTitle("Error");
					    alert2.setContentText(e.getMessage());
					    alert2.showAndWait();
					}
			    }
			});
		}
		else{
			mapManager.setVertexDoubleClickAction(null);
		}
	}

	// Event Listener on Button[#addRouteButton].onAction
	@FXML
	public void addRoute(ActionEvent event) {
		MapManager mapManager = MapManager.getInstance();
		if(addRouteButton.isSelected()) {
			mapManager.setVertexDoubleClickAction((SmartGraphVertex<BusStop> v) -> {
				SelectTwoStop.addStop(v.getUnderlyingVertex().element());
				System.out.println(v.getUnderlyingVertex().element());
				if(SelectTwoStop.full()) {
					Stage stage = new Stage();
					stage.initModality(Modality.APPLICATION_MODAL);
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/route/addRoute.fxml"));
						Parent root = loader.load();
				        addRouteController controller = loader.getController();
				        controller.setSourceStop(SelectTwoStop.getSourceStop());
				        controller.setDestinationStop(SelectTwoStop.getDestinationStop());
						Scene scene =  new Scene(root);
				        stage.setScene(scene);
				        stage.showAndWait();
					} catch (IOException e) {
						e.printStackTrace();
					}
					SelectTwoStop.reset();
				}
			});
		}
		else{
			mapManager.setVertexDoubleClickAction(null);
		}
	}
	// Event Listener on Button[#modRouteButton].onAction
	@FXML
	public void modRoute(ActionEvent event) {
		MapManager mapManager = MapManager.getInstance();
		if(modRouteButton.isSelected()) {
			mapManager.setEdgeDoubleClickAction((SmartGraphEdge<Route,BusStop> ed) ->{
				Stage stage = new Stage();
				stage.initModality(Modality.APPLICATION_MODAL);
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/route/modRoute.fxml"));
					Parent root = loader.load();
			        modRouteController controller = loader.getController();
			        controller.setRoute(ed.getUnderlyingEdge().element());
					Scene scene =  new Scene(root);
			        stage.setScene(scene);
			        stage.showAndWait();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		else{
			mapManager.setEdgeDoubleClickAction(null);
		}
	}
	// Event Listener on Button[#delRouteButton].onAction
	@FXML
	public void delRoute(ActionEvent event) {
		MapManager mapManager = MapManager.getInstance();
		if(delRouteButton.isSelected()) {
			mapManager.setEdgeDoubleClickAction((SmartGraphEdge<Route,BusStop> ed) -> {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			    alert.setHeaderText(null);
			    alert.setTitle("Eliminación de Calle");
			    alert.setContentText("Desea eliminar la calle que conecta "+ed.getUnderlyingEdge().element()+"?");
			    Optional<ButtonType> action = alert.showAndWait();
			    if (action.get() == ButtonType.OK) {
			    	RouteDao routeDao = new RouteDaoPG();
			    	try {
						routeDao.deleteData(ed.getUnderlyingEdge().element());
						mapManager.deleteRouteMap(ed.getUnderlyingEdge());
					} catch (DeleteFailException|DBConnectionException e) {
						Alert alert2 = new Alert(Alert.AlertType.ERROR);
					    alert2.setHeaderText(null);
					    alert2.setTitle("Error");
					    alert2.setContentText(e.getMessage());
					    alert2.showAndWait();
					}
			    }
			});
		}
		else{
			mapManager.setEdgeDoubleClickAction(null);
		}
	}
	// Event Listener on Button[#addIncidentButton].onAction
	@FXML
	public void addIncident(ActionEvent event) {
		MapManager mapManager = MapManager.getInstance();
		if(addIncidentButton.isSelected()) {
			mapManager.setVertexDoubleClickAction((SmartGraphVertex<BusStop> v) -> {
				Stage stage = new Stage();
				stage.initModality(Modality.APPLICATION_MODAL);
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/incident/addIncident.fxml"));
					Parent root = loader.load();
					addIncidentController controller = loader.getController();
					controller.setStop(v.getUnderlyingVertex().element());
					Scene scene =  new Scene(root);
			        stage.setScene(scene);
			        stage.showAndWait();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		else{
			mapManager.setVertexDoubleClickAction(null);
		}
	}
	// Event Listener on Button[#showIncidentButton].onAction
	@FXML
	public void showIncident(ActionEvent event) {
		Stage stage = (Stage) borderPane.getScene().getWindow();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/views/incident/showIncident.fxml"));
			Scene scene =  new Scene(root);
	        stage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
