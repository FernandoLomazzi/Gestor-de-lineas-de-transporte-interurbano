package controllers.line;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import models.busline.CheapLine;

public class modLineCheapController extends modLineController {
	@FXML
	private Label standingLabel;
	@FXML
	private Label standingPorcentageLabel;
	@FXML
	private Slider standingSlider;
	private IntegerProperty seatingCapacityProperty;
	private StringProperty standingCapacityProperty;
	private CheapLine cheapLineToModify;
	private CheapLine modifiedCheapLine;
	private class CheapLineListener implements ChangeListener<Object> {
		@Override
		public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object arg2) {
			modifiedCheapLine.setStandingCapacityPercentage(Double.parseDouble(standingCapacityProperty.getValue().trim().replace(',','.')));
			System.out.println(0);
			if (modifiedCheapLine.validateChanges(cheapLineToModify)) {
				System.out.println(1);
				confirmChangesButton.setDisable(true);
			}
			else {
				confirmChangesButton.setDisable(false);
				System.out.println(2);
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
		standingCapacityProperty.addListener(new CheapLineListener());
	}

	@Override
	@FXML
	protected void confirmChanges(ActionEvent event) {

	}
	
	@Override
	@FXML
	protected void restoreChanges(ActionEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	public void setCheapLine(CheapLine cheapLineToModify) {
		this.cheapLineToModify = cheapLineToModify;
		this.modifiedCheapLine = new CheapLine();
		setBusLine(cheapLineToModify, modifiedCheapLine);
	}
}
