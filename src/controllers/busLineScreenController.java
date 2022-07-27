package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class busLineScreenController implements Initializable,returnScene{

    @FXML
    private Button addBusLineButton;
    @FXML
    private Button deleteBusLineButton;
    @FXML
    private Button modifyBusLineButton;
    @FXML
    private Button goToPrevSceneButton;
    
    private Scene prevScene;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

    @FXML
    void addBusLine(ActionEvent event) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/views/line/addLine.fxml"));
        try {
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
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
      	Stage stage = new Stage();
    	FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/views/line/showLine.fxml"));
    	try {
    		AnchorPane root = loader.load();
    		Scene scene = new Scene(root);
    		stage.initModality(Modality.APPLICATION_MODAL);
    		stage.setTitle("Eliminar Linea");
    		stage.setScene(scene);
    		stage.setMaximized(false);
    		stage.setResizable(false);
    		stage.showAndWait();
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void modifyBusLine(ActionEvent event) {

    }

	@Override
	public void setPrevScene(Scene scene) {
		this.prevScene=scene;
	}
	
	@FXML
	@Override
	public void goToPrevScene(ActionEvent event) {
			((Stage) goToPrevSceneButton.getScene().getWindow()).setScene(prevScene);
	}



}