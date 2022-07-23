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

import db.dao.BusStopDao;
import db.dao.DBConnection;
import db.dao.IncidentDao;
import db.dao.impl.BusStopDaoPG;
import db.dao.impl.IncidentDaoPG;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import managers.MapManager;
import models.BusStop;
import models.Incident;
import models.Route;
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
	public static Scene scene;
	@Override
	public void start(Stage primaryStage) {
		try {
			/*			
			BusStop bus1 = new BusStop(1,"calle1",111,true);
			BusStop bus2 = new BusStop(2,"calle2",222,true);
			BusStop bus3 = new BusStop(3,"calle3",333,true);
			Route route1 = new Route(bus1,bus2,23.0,true);
			Route route2 = new Route(bus2,bus3,5.0,true);
			g = new DigraphEdgeList<>();
			g.insertVertex(bus1);
			g.insertVertex(bus2);
			g.insertVertex(bus3);
			g.insertEdge(bus1, bus2, route1);
			g.insertEdge(bus2, bus3, route2);
			graphView = new SmartGraphPanel<>(g,new SmartCircularSortedPlacementStrategy());
			graphView.setVertexDoubleClickAction((v)->{
				System.out.println(v.getUnderlyingVertex());
			});
			Scene scene = new Scene(graphView,500,500);
			primaryStage.setScene(scene);
			primaryStage.show();
			graphView.init();
			*/
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mainScreen.fxml"));
			BorderPane root = loader.load();
			MapManager mapManager = MapManager.getInstance();
			root.setCenter(mapManager.getMapView());
            scene = new Scene(root);
            primaryStage.setTitle("MainScreen");
            primaryStage.setScene(scene);
            primaryStage.show();
            mapManager.getMapView().init();
            mapManager.initStyleMap();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
