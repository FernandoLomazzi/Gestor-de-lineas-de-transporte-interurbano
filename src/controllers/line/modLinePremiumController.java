package controllers.line;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.paint.Color;
import models.busline.PremiumLine;

public class modLinePremiumController implements Initializable{
	private ObjectProperty<Color> colorProperty;
	private IntegerProperty seatingCapacityProperty;
	private BooleanProperty wifiServiceProperty;
	private BooleanProperty airServiceProperty;
	private PremiumLine premiumLineToModify;
	private PremiumLine modifiedPremiumLine;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		colorProperty.bind(color.valueProperty());
		seatingCapacityProperty.bind(seatingCapacity.valueProperty());
		wifiServiceProperty.bind(wifiService.selectedProperty());
		airServiceProperty.bind(airService.selectedProperty());
	}
	@FXML
    private CheckBox airService;
    @FXML
    private ColorPicker color;
    @FXML
    private Button confirmChangesButton;
    @FXML
    private Button goBackButton;
    @FXML
    private Button restoreChangesButton;
    @FXML
    private Spinner<Integer> seatingCapacity;
    @FXML
    private Label servicesLabel;
    @FXML
    private CheckBox wifiService;
    @FXML
	public static final Integer MIN_SLIDER = 1;
	public static final Integer MAX_SLIDER = 200;
    void confirmChanges(ActionEvent event) {

    }
    @FXML
    void goBack(ActionEvent event) {

    }
    @FXML
    void restoreChanges(ActionEvent event) {

    }
    void setBusLine(PremiumLine premiumLineToModify) {
    	this.premiumLineToModify = premiumLineToModify;
		
		SpinnerValueFactory<Integer> valueFactory = 
				new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_SLIDER, MAX_SLIDER);
		valueFactory.setValue(premiumLineToModify.getSeatingCapacity());
		seatingCapacity.setValueFactory(valueFactory);
    }

}
