package application;
	
import src.com.brunomnsilva.smartgraph.graph.Digraph;
import src.com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import src.com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import src.com.brunomnsilva.smartgraph.graph.Vertex;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;

import controllers.line.LineRouteSelectorController;
import db.dao.BusStopDao;
import db.dao.DBConnection;
import db.dao.IncidentDao;
import db.dao.impl.BusStopDaoPG;
import db.dao.impl.IncidentDaoPG;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import managers.LineMapSelectorManager;
import managers.CityMapManager;
import models.BusStop;
import models.Incident;
import models.Route;
import models.busline.BusLine;
import models.busline.CheapLine;
import models.utils.MyHeap;
import models.utils.SelectTwoStop;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;


public class Main extends Application {
	/*@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mainScreen.fxml"));
			BorderPane root = loader.load();
			MapManager mapManager = MapManager.getInstance();
			root.setCenter(mapManager.getMapView());
            Scene scene = new Scene(root);
            primaryStage.setTitle("MainScreen");
            primaryStage.setScene(scene);
            primaryStage.show();
            mapManager.getMapView().init();
            mapManager.initStyleMap();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}*/
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/line/LineRouteSelector.fxml"));
			BorderPane root = loader.load();
			LineRouteSelectorController controller = loader.getController();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
            controller.init(new CheapLine("LINEA 1","0x16a216"));
            
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
