package application;
	
import src.com.brunomnsilva.smartgraph.graph.Digraph;
import src.com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import src.com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import src.com.brunomnsilva.smartgraph.graph.Vertex;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.HashSet;
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
import models.busline.PremiumLine.PremiumLineService;
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
	public static Stage mainStage;
	@Override
	public void start(Stage primaryStage) {
		try {
			mainStage = primaryStage; 
			AnchorPane root = FXMLLoader.load(getClass().getResource("/views/mainScreen.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();            
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
