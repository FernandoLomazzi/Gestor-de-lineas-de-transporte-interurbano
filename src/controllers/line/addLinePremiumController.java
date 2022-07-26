package controllers.line;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import models.busline.PremiumLine;

public class addLinePremiumController implements Initializable{

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
	@FXML
    private Button addLinePremium;

    @FXML
    private CheckBox addLinePremiumAirField;

    @FXML
    private ColorPicker addLinePremiumColorField;

    @FXML
    private TextField addLinePremiumNameField;

    @FXML
    private TextField addLinePremiumSeatingCapacityField;

    @FXML
    private CheckBox addLinePremiumWifiField;

    @FXML
    void addNewLinePremium(ActionEvent event) {
    	
    }
}
