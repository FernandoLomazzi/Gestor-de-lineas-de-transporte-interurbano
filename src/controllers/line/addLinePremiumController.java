package controllers.line;

import java.net.URL;
import java.util.ResourceBundle;

import db.dao.PremiumLineDao;
import db.dao.impl.PremiumLineDaoPG;
import exceptions.AddFailException;
import exceptions.DBConnectionException;
import exceptions.EmptyFieldException;
import exceptions.IncompleteFieldException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import managers.AlertManager;
import models.busline.PremiumLine;
import models.busline.PremiumLine.PremiumLineService;

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
    	PremiumLine premiumLine = new PremiumLine();
	    try {
	    	if (addLinePremiumNameField.getText().trim().equals("")) {
				throw new EmptyFieldException("Nombre vacio");
			}
	    	premiumLine.setName(addLinePremiumNameField.getText().trim());
			premiumLine.setColor(addLinePremiumColorField.getValue().toString());
			premiumLine.setSeatingCapacity(Integer.parseInt(addLinePremiumSeatingCapacityField.getText().trim()));
			if (!addLinePremiumWifiField.isSelected() && !addLinePremiumAirField.isSelected()) {
				throw new IncompleteFieldException("Debe seleccionar al menos un servicio");
			}
			if (addLinePremiumWifiField.isSelected()) {
				premiumLine.getServices().add(PremiumLineService.WIFI);
			}
			if (addLinePremiumAirField.isSelected()) {
				premiumLine.getServices().add(PremiumLineService.AIR_CONDITIONING);
			}
	    }
	    catch(NumberFormatException e) {
			AlertManager.createAlert(AlertType.ERROR, "Error", "Ingrese números para la cantidad máxima de pasajeros sentados");
			return;
		}
    	catch(EmptyFieldException e) {
			AlertManager.createAlert(AlertType.ERROR, "Error", e.getMessage());
			return;
    	}
	    catch(IncompleteFieldException e) {
	    	AlertManager.createAlert(AlertType.ERROR, "Error", e.getMessage());
			return;
	    }
		PremiumLineDao premiumLineDao = new PremiumLineDaoPG();
	    try {
	    	premiumLineDao.addData(premiumLine);
		}
	    catch(AddFailException | DBConnectionException e) {
    		AlertManager.createAlert(AlertType.ERROR, "Error", e.getMessage());
		    return;
	    }
	    AlertManager.createAlert(AlertType.INFORMATION, "Exito", "Se añadió a la linea correctamente");
	    ((Stage) (addLinePremium.getScene().getWindow())).close();
    }
}


























