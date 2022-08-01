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
	private BusLine busLineToModify;
	private BusLine modifiedBusLine;
	abstract protected void confirmChanges(ActionEvent event);
	abstract protected void restoreChanges(ActionEvent event);
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
		seatingCapacity.setValueFactory(valueFactory);
		
		colorProperty = new SimpleObjectProperty<Color>();
		colorProperty.bind(color.valueProperty());
		colorProperty.addListener(new BusLineListener());
		seatingCapacityProperty = new SimpleIntegerProperty();
		seatingCapacityProperty.bind(seatingCapacity.valueProperty());
		seatingCapacityProperty.addListener(new BusLineListener());
		colorProperty.addListener(new BusLineListener());
		seatingCapacityProperty.addListener(new BusLineListener());
		
		confirmChangesButton.setDisable(true);
	}

	private class BusLineListener implements ChangeListener<Object> {
		@Override
		public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object arg2) {
			System.out.println("Cambio");
			modifiedBusLine.setColor(colorProperty.getValue());
			modifiedBusLine.setSeatingCapacity(seatingCapacityProperty.getValue());
		}
	}
	
	protected void setBusLine(BusLine busLineToModify, BusLine modifiedBusLine) {
		this.busLineToModify = busLineToModify;
		this.modifiedBusLine = modifiedBusLine;
		//Setear color cuando venga fer
		seatingCapacity.getValueFactory().setValue(busLineToModify.getSeatingCapacity());
	}

	@FXML
    protected void goBack(ActionEvent event) {
    	((Stage)confirmChangesButton.getScene().getWindow()).close();
    };
}
