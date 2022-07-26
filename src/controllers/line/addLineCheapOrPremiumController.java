package controllers.line;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class addLineCheapOrPremiumController {

    @FXML
    private Button addCheapButton;

    @FXML
    private Button addPremiumButton;

    @FXML
    void addCheap(ActionEvent event) {
    	Stage stage = (Stage)addCheapButton.getScene().getWindow();
    	stage.close();
    	stage = new Stage();
    	stage.initModality(Modality.APPLICATION_MODAL);
    	FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/views/line/addLineCheap.fxml"));
    	try {
			Parent root = loader.load();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setTitle("Agregar linea económica");
			stage.show();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void addPremium(ActionEvent event) {
    	Stage stage = (Stage)addCheapButton.getScene().getWindow();
    	stage.close();
    	stage = new Stage();
    	stage.initModality(Modality.APPLICATION_MODAL);
    	FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/views/line/addLinePremium.fxml"));
    	try {
			Parent root = loader.load();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setTitle("Agregar linea superior");
			stage.show();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }

}
