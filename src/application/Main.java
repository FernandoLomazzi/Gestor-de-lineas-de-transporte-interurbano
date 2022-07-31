package application;
	
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.BusStop;


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
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {		
		launch(args);
	}
}
