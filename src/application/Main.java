package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
	public static Stage mainStage;

	@Override
	public void start(Stage primaryStage) {
		try {
			mainStage = primaryStage; 
			AnchorPane root = FXMLLoader.load(getClass().getResource("/views/mainScreen.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setMaximized(true);
            primaryStage.setTitle("Gestor de líneas de colectivos");
            primaryStage.getIcons().add(new Image("/icon.png"));
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

