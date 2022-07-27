package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class busLineScreenController {

    @FXML
    private Button addBusLineButton;

    @FXML
    private Button deleteBusLineButton;

    @FXML
    private Button modifyBusLineButton;

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

}