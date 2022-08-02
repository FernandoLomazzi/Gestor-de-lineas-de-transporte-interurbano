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
import controllers.incident.showIncidentController;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import managers.AlertManager;
import managers.CityMapManager;
import managers.StageManager;
import models.BusStop;
import models.Route;
import models.utils.SelectTwoStop;

public class cityMapScreenController implements Initializable,returnScene{
	@FXML 
	private BorderPane borderPane;
	@FXML 
	private Button addStopButton;
	@FXML 
	private Button showIncidentButton;
	@FXML 
	private Button goToPrevSceneButton;
	@FXML 
	private ToggleGroup toggleGroup;
	@FXML
	private ToggleButton addRouteButton;
	@FXML 
	private ToggleButton addIncidentButton;
	@FXML 
	private ToggleButton modifyButton;
	@FXML 
	private ToggleButton deleteButton;
	
	private Scene prevScene;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		borderPane.setCenter(CityMapManager.getInstance().getMapView());
		//Reveer
		modifyButton.setToggleGroup(toggleGroup);
		deleteButton.setToggleGroup(toggleGroup);
		addRouteButton.setToggleGroup(toggleGroup);
		addIncidentButton.setToggleGroup(toggleGroup);
	}
	// Event Listener on Button[#addStopButton].onAction
	@FXML
	public void addStop(ActionEvent event) {
		Stage stage = new Stage(StageStyle.UTILITY);
		stage.initModality(Modality.APPLICATION_MODAL);
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/views/stop/addBusStop.fxml"));
			Scene scene =  new Scene(root);
			stage.setTitle("Creación de parada de colectivos");
	        stage.setScene(scene);
	        stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void modify(ActionEvent event) {
		CityMapManager cityMapManager = CityMapManager.getInstance();
		if(modifyButton.isSelected()) {
			cityMapManager.setVertexDoubleClickAction((SmartGraphVertex<BusStop> v) -> {
				Stage stage = new Stage(StageStyle.UTILITY);
				stage.initModality(Modality.APPLICATION_MODAL);
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/stop/modBusStop.fxml"));
					Parent root = loader.load();
			        modBusStopController controller = loader.getController();
			        controller.setBusStop(v.getUnderlyingVertex().element());
					Scene scene =  new Scene(root);
					stage.setTitle("Modificación de parada de colectivos");
			        stage.setScene(scene);
			        stage.showAndWait();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			cityMapManager.setEdgeDoubleClickAction((SmartGraphEdge<Route,BusStop> ed) ->{
				Stage stage = new Stage(StageStyle.UTILITY);
				stage.initModality(Modality.APPLICATION_MODAL);
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/route/modRoute.fxml"));
					Parent root = loader.load();
			        modRouteController controller = loader.getController();
			        controller.setRoute(ed.getUnderlyingEdge().element());
					Scene scene =  new Scene(root);
					stage.setTitle("Modificación de calle");
			        stage.setScene(scene);
			        stage.showAndWait();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}
	@FXML
	public void delete(ActionEvent event) {
		CityMapManager cityMapManager = CityMapManager.getInstance();
		if(deleteButton.isSelected()) {
			cityMapManager.setVertexDoubleClickAction((SmartGraphVertex<BusStop> v) -> {
				Alert alert = AlertManager.createAlert(AlertType.CONFIRMATION, "Eliminación de parada de colectivos", "Desea eliminar la parada de colectivo "+v.getUnderlyingVertex().element()+"?");
			    Optional<ButtonType> action = alert.showAndWait();
			    if (action.get() == ButtonType.OK) {
			    	BusStopDao busStopDao = new BusStopDaoPG();
			    	try {
						busStopDao.deleteData(v.getUnderlyingVertex().element());
				    	cityMapManager.deleteStopMap(v.getUnderlyingVertex());
					} catch (DeleteFailException|DBConnectionException e) {
						AlertManager.createAlert(AlertType.ERROR, "Error", e.getMessage()).showAndWait();
					}
			    }
			});
			cityMapManager.setEdgeDoubleClickAction((SmartGraphEdge<Route,BusStop> ed) -> {
				Alert alert = AlertManager.createAlert(AlertType.CONFIRMATION, "Eliminación de calle", "Desea eliminar la calle que conecta "+ed.getUnderlyingEdge().element()+"?");
			    Optional<ButtonType> action = alert.showAndWait();
			    if (action.get() == ButtonType.OK) {
			    	RouteDao routeDao = new RouteDaoPG();
			    	try {
						routeDao.deleteData(ed.getUnderlyingEdge().element());
						cityMapManager.deleteRouteMap(ed.getUnderlyingEdge());
					} catch (DeleteFailException|DBConnectionException e) {
						AlertManager.createAlert(AlertType.ERROR, "Error", e.getMessage()).showAndWait();
					}
			    }
			});
		}
	}

	@FXML
	public void addRoute(ActionEvent event) {
		CityMapManager cityMapManager = CityMapManager.getInstance();
		if(addRouteButton.isSelected()) {
			cityMapManager.setVertexDoubleClickAction((SmartGraphVertex<BusStop> v) -> {
				SelectTwoStop.addStop(v.getUnderlyingVertex().element());
				System.out.println(v.getUnderlyingVertex().element());
				if(SelectTwoStop.full()) {
					try {
						Stage stage = new Stage(StageStyle.UTILITY);
						stage.setTitle("Creación de calle");
						stage.initModality(Modality.APPLICATION_MODAL);
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/route/addRoute.fxml"));
						Parent root = loader.load();
				        addRouteController controller = loader.getController();
				        controller.setSourceStop(SelectTwoStop.getSourceStop());
				        controller.setDestinationStop(SelectTwoStop.getDestinationStop());
						Scene scene = new Scene(root);
						stage.setTitle("Creación de calle");
				        stage.setScene(scene);
				        stage.showAndWait();
					} catch (IOException e) {
						e.printStackTrace();
					}
					SelectTwoStop.reset();
				}
			});
			cityMapManager.setEdgeDoubleClickAction(null);
		}
	}
	// Event Listener on Button[#addIncidentButton].onAction
	@FXML
	public void addIncident(ActionEvent event) {
		CityMapManager cityMapManager = CityMapManager.getInstance();
		if(addIncidentButton.isSelected()) {
			cityMapManager.setVertexDoubleClickAction((SmartGraphVertex<BusStop> v) -> {
				Stage stage = new Stage(StageStyle.UTILITY);
				stage.initModality(Modality.APPLICATION_MODAL);
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/incident/addIncident.fxml"));
					Parent root = loader.load();
					addIncidentController controller = loader.getController();
					controller.setStop(v.getUnderlyingVertex().element());
					Scene scene =  new Scene(root);
					stage.setTitle("Registro de incidencia");
			        stage.setScene(scene);
			        stage.showAndWait();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			cityMapManager.setEdgeDoubleClickAction(null);
		}
	}
	// Event Listener on Button[#showIncidentButton].onAction
	@FXML
	public void showIncident(ActionEvent event) {
		Stage stage = (Stage) borderPane.getScene().getWindow();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/incident/showIncident.fxml"));
			Parent root = loader.load();
			showIncidentController controller = loader.getController();
			controller.setPrevScene(showIncidentButton.getScene());
			Scene scene = new Scene(root);
			stage.setTitle("Visualización de incidencias activas");
	        stage.setScene(scene);
	        StageManager.updateMainStage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void setPrevScene(Scene scene) {
		this.prevScene=scene;
	}
	@Override
	public void goToPrevScene(ActionEvent event) {
		Stage stage = (Stage) goToPrevSceneButton.getScene().getWindow();
		stage.setTitle("Gestor de líneas de colectivos");
		stage.setScene(prevScene);
		StageManager.updateMainStage();
	}
}
