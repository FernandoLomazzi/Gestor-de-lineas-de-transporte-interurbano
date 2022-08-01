package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.BusStop;
import models.Route;
import src.com.brunomnsilva.smartgraph.containers.*;
import src.com.brunomnsilva.smartgraph.graph.Digraph;
import src.com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import src.com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import src.com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;


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
            primaryStage.setMaximized(true);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {		
		launch(args);
	}
}

