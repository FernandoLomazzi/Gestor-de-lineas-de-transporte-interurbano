package controllers.line;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import managers.LineMapManager;
import models.busline.BusLine;

public abstract class modLineController implements Initializable{
	@FXML
	protected ColorPicker color;
	@FXML
    protected Button confirmChangesButton;
	@FXML
    protected Button goBackButton;
	@FXML
    protected Button restoreChangesButton;
	@FXML
    protected Spinner<Integer> seatingCapacity;
	protected ObjectProperty<Color> colorProperty;
	protected IntegerProperty seatingCapacityProperty;
	protected LineMapManager lineMapManager;
	private BusLine busLineToModify;
	private BusLine modifiedBusLine;
	public static final Integer MIN_SPINNER = 1;
	public static final Integer MAX_SPINNER = 200;
	
	@FXML
    protected void goBack(ActionEvent event) {
    	((Stage)confirmChangesButton.getScene().getWindow()).close();
    };
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_SPINNER, MAX_SPINNER);
		seatingCapacity.setValueFactory(valueFactory);
		
		colorProperty = new SimpleObjectProperty<Color>();
		colorProperty.bind(color.valueProperty());
		seatingCapacityProperty = new SimpleIntegerProperty();
		seatingCapacityProperty.bind(seatingCapacity.valueProperty());
		
		confirmChangesButton.setDisable(true);
	}

	protected void confirmChanges() {
		busLineToModify.setColor(modifiedBusLine.getColor());
		busLineToModify.setSeatingCapacity(modifiedBusLine.getSeatingCapacity());
	}

	protected void setBusLine(BusLine busLineToModify, BusLine modifiedBusLine) {
		this.busLineToModify = busLineToModify;
		this.modifiedBusLine = modifiedBusLine;
	}

	protected void setListeners(ChangeListener<Object> listener) {
		colorProperty.addListener(listener);
		seatingCapacityProperty.addListener(listener);
		colorProperty.addListener(listener);
	}

	protected void restoreChanges() {
		color.setValue(busLineToModify.getColor());
		seatingCapacity.getValueFactory().setValue(busLineToModify.getSeatingCapacity());
	}

	protected void changedBusLine() {
		modifiedBusLine.setColor(colorProperty.getValue());
		modifiedBusLine.setSeatingCapacity(seatingCapacityProperty.getValue());
	}

	public void setLineMapManager(LineMapManager lineMapManager) {
		this.lineMapManager = lineMapManager;
	}
}
