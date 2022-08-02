package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import controllers.line.addLineController;
import controllers.line.showLineController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import managers.LineMapManager;
import managers.StageManager;
import src.com.brunomnsilva.smartgraph.containers.*;

public class busLineScreenController implements Initializable,returnScene{
	@FXML
	private GridPane gridPane;
    @FXML
    private Button addBusLineButton;
    @FXML
    private Button deleteBusLineButton;
    @FXML
    private Button modifyBusLineButton;
    @FXML
    private Button goToPrevSceneButton;
    
    private Scene prevScene;
    private LineMapManager lineMapManager;
    

    public busLineScreenController() {
    	lineMapManager = new LineMapManager();
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		gridPane.add(lineMapManager.getMapView(), 0, 1);
	}
	
    public LineMapManager getLineMapManager() {
    	return this.lineMapManager;
    }

    @FXML
    void addBusLine(ActionEvent event) {
    	try {
    		Stage stage = new Stage(StageStyle.UTILITY);
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/views/line/addLine.fxml"));
            AnchorPane root = loader.load();
            System.out.println(lineMapManager);
            addLineController controller = loader.getController();
            controller.setManager(this.lineMapManager);
            Scene scene = new Scene(root);
            stage.setTitle("Creaci�n de l�nea de colectivos");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteBusLine(ActionEvent event) {
    	Stage stage = Main.mainStage;
    	FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/views/line/showLine.fxml"));
    	try {
    		BorderPane root = loader.load();
    		showLineController controller = loader.getController();
    		controller.setManager(lineMapManager);
    		controller.setOperation(showLineController.avalibleOperations.DELETE);
    		controller.setPrevScene(deleteBusLineButton.getScene());
    		Scene scene = new Scene(root);
    		stage.setTitle("Eliminaci�n de l�nea de colectivos");
    		stage.setScene(scene);
    		StageManager.updateMainStage();
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void modifyBusLine(ActionEvent event) {
    	Stage stage = Main.mainStage;
    	FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/views/line/showLine.fxml"));
    	try {
    		BorderPane root = loader.load();
    		showLineController controller = loader.getController();
    		controller.setManager(lineMapManager);
    		controller.setOperation(showLineController.avalibleOperations.MODIFY);
    		controller.setPrevScene(modifyBusLineButton.getScene());
    		Scene scene = new Scene(root);
    		stage.setTitle("Modificaci�n de l�nea de colectivos");
    		stage.setScene(scene);
    		StageManager.updateMainStage();
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
    }
	@Override
	public void setPrevScene(Scene scene) {
		this.prevScene=scene;
	}
	@FXML
	@Override
	public void goToPrevScene(ActionEvent event) {
		Stage stage = (Stage) goToPrevSceneButton.getScene().getWindow();
		stage.setTitle("Gestor de l�neas de colectivos");
		stage.setScene(prevScene);
		StageManager.updateMainStage();
	}
}