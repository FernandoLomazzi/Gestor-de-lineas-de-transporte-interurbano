package controllers.line;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import models.busline.CheapLine;

public class modLineCheapController extends modLineController {
	@FXML
	private Label standingLabel;
	@FXML
	private Label standingPorcentageLabel;
	@FXML
	private Slider standingSlider;
	private StringProperty standingCapacityProperty;
	private CheapLine cheapLineToModify;
	private CheapLine modifiedCheapLine;
	private class CheapLineListener implements ChangeListener<Object> {
		@Override
		public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object arg2) {
			modifiedCheapLine.setStandingCapacityPercentage(Double.parseDouble(standingCapacityProperty.getValue().trim().replace(',','.'))/100);
			if (modifiedCheapLine.validateChanges(cheapLineToModify)) {
				confirmChangesButton.setDisable(true);
			}
			else {
				confirmChangesButton.setDisable(false);
			}
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
		
		standingPorcentageLabel.setText(String.valueOf(standingSlider.getValue()));
		standingSlider.setMax(CheapLine.getMaxStandingCapacityPercentage()*100);
		standingPorcentageLabel.textProperty().bind(standingSlider.valueProperty().asString("%.1f"));
		
		standingCapacityProperty = new SimpleStringProperty();
		standingCapacityProperty.bind(standingPorcentageLabel.textProperty());
	}

	@FXML
	protected void confirmChanges(ActionEvent event) {
		super.confirmChanges();
		cheapLineToModify.setStandingCapacityPercentage(modifiedCheapLine.getStandingCapacityPercentage());
		lineMapManager.chargeLine(cheapLineToModify);
    	((Stage)standingLabel.getScene().getWindow()).close();
	}
	
	@FXML
	protected void restoreChanges(ActionEvent event) {
		super.restoreChanges();
		standingSlider.setValue(cheapLineToModify.getStandingCapacityPercentage()*100);
	}
	
	public void setCheapLine(CheapLine cheapLineToModify) {
		this.cheapLineToModify = cheapLineToModify;
		this.modifiedCheapLine = new CheapLine();
		setBusLine(cheapLineToModify, modifiedCheapLine);
		
		standingSlider.setValue(cheapLineToModify.getStandingCapacityPercentage()*100);
		super.restoreChanges();

		standingCapacityProperty.addListener(new CheapLineListener());
		colorProperty.addListener(new CheapLineListener());
		seatingCapacityProperty.addListener(new CheapLineListener());
	}
}
