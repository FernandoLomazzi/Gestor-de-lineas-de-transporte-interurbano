package controllers.line;

import java.net.URL;
import java.util.ResourceBundle;

import db.dao.CheapLineDao;
import db.dao.impl.CheapLineDaoPG;
import exceptions.AddFailException;
import exceptions.DBConnectionException;
import exceptions.DeleteFailException;
import exceptions.EmptyFieldException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import managers.AlertManager;
import models.busline.CheapLine;

public class addLineCheapController implements Initializable{

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.updateSlider();
		this.addLineCheapStandingCapacityLabel.textProperty().bind(addLineCheapStandingCapacity.valueProperty().asString("%.1f"));
	}
    @FXML
    private Button addLineCheap;

    @FXML
    private ColorPicker addLineCheapColorField;

    @FXML
    private TextField addLineCheapNameField;

    @FXML
    private TextField addLineCheapSeatingCapacityField;

    @FXML
    private Slider addLineCheapStandingCapacity;
    
    @FXML
    private Label addLineCheapStandingCapacityLabel;
    
    private void updateSlider() {
    	this.addLineCheapStandingCapacityLabel.setText(String.valueOf(addLineCheapStandingCapacity.getValue()));
    }
    @FXML
    void addNewLineCheap(ActionEvent event) {
    	CheapLine cheapLine = new CheapLine();
    	try {
    		if (addLineCheapNameField.getText().trim().equals("")) {
    			throw new EmptyFieldException("Nombre vacio");
    		}
			cheapLine.setName(addLineCheapNameField.getText().trim());
			cheapLine.setColor(addLineCheapColorField.getValue().toString());
			cheapLine.setSeatingCapacity(Integer.parseInt(addLineCheapSeatingCapacityField.getText().trim()));
			cheapLine.setStandingCapacityPercentage(Double.parseDouble(addLineCheapStandingCapacityLabel.getText().replaceAll(",",".")));
			//Este no deberia tener errores nunca
			
		}catch(NumberFormatException e) {
			AlertManager.createAlert(AlertType.ERROR, "Error", "Ingrese números para la cantidad máxima de pasajeros sentados");
			return;
		}
    	catch(EmptyFieldException e) {
			AlertManager.createAlert(AlertType.ERROR, "Error", e.getMessage());
			return;
    	}
    	CheapLineDao cheapLineDao = new CheapLineDaoPG();
    	/*try {
    		cheapLineDao.addData(cheapLine);
    		//Cosas del mapa
    	}
    	catch(AddFailException | DBConnectionException e) {
    		AlertManager.createAlert(AlertType.ERROR, "Error", e.getMessage());
		    return;
    	}*/
    	try {
			cheapLineDao.deleteData(cheapLine);
		} catch (DeleteFailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DBConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	AlertManager.createAlert(AlertType.INFORMATION, "Exito", "Se añadió a la linea correctamente");
	    ((Stage) (addLineCheap.getScene().getWindow())).close();
    }
}
