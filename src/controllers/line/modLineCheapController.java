package controllers.line;

import java.net.URL;
import java.util.ResourceBundle;

import db.dao.CheapLineDao;
import db.dao.impl.CheapLineDaoPG;
import exceptions.DBConnectionException;
import exceptions.ModifyFailException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import managers.AlertManager;
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
			changedBusLine();
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

    	CheapLineDao cheapLineDao = new CheapLineDaoPG();
    	try {
    		cheapLineDao.modifyData(cheapLineToModify);
    	}
    	catch(DBConnectionException | ModifyFailException e) {
			AlertManager.createAlert(AlertType.ERROR, "ERROR", e.getMessage());
			return;
    	}
    	AlertManager.createAlert(AlertType.INFORMATION, "EXITO", "Se modificó la linea exitosamente.");
    	((Stage)standingLabel.getScene().getWindow()).close();
	}
	
	@FXML
	protected void restoreChanges(ActionEvent event) {
		System.out.println(cheapLineToModify.getStandingCapacityPercentage());
		super.restoreChanges();
		standingSlider.setValue(cheapLineToModify.getStandingCapacityPercentage()*100);
	}
	
	public void setCheapLine(CheapLine cheapLineToModify) {
		this.cheapLineToModify = cheapLineToModify;
		this.modifiedCheapLine = new CheapLine();
		setBusLine(cheapLineToModify, modifiedCheapLine);
		
		standingSlider.setValue(cheapLineToModify.getStandingCapacityPercentage()*100);
		super.restoreChanges();

		setListeners(new CheapLineListener());
	}
	
	@Override
	public void setListeners(ChangeListener<Object> listener) {
		super.setListeners(listener);
		standingCapacityProperty.addListener(listener);
	}
}
