package controllers.line;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import exceptions.EmptyFieldException;
import exceptions.IncompleteFieldException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import managers.AlertManager;
import managers.LineMapManager;
import managers.StageManager;
import models.busline.BusLine;
import models.busline.CheapLine;
import models.busline.PremiumLine;
import models.busline.PremiumLine.PremiumLineService;

public class addLineController implements Initializable{
	
    @FXML
    private ChoiceBox<String> typeLineBox;
    @FXML
    private ColorPicker lineColorField;
    @FXML
    private TextField lineNameField;
    @FXML
    private TextField lineSeatingCapacityField;
    @FXML
    private VBox cheapBox;
    @FXML
    private VBox premiumBox;
    @FXML
    private Slider lineCheapStandingCapacity;
    @FXML
    private Label lineCheapStandingCapacityLabel;
    @FXML
    private CheckBox linePremiumAirBox;
    @FXML
    private CheckBox linePremiumWifiBox;
    @FXML
    private Button cancelButton;
    @FXML
    private Button nextButton;
    
    private BusLine busLine;
    LineMapManager manager;
   
    private static String[] lineTypes = {"Económica","Superior"}; 
   
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lineCheapStandingCapacityLabel.setText(String.valueOf(lineCheapStandingCapacity.getValue()));
		//Se espera que el valor maximo siempre sea acotado y se encuentre entre [0..1]
		lineCheapStandingCapacity.setMax(CheapLine.getMaxStandingCapacityPercentage()*100);
		lineCheapStandingCapacityLabel.textProperty().bind(lineCheapStandingCapacity.valueProperty().asString("%.1f"));
		typeLineBox.getItems().addAll(lineTypes);
		typeLineBox.setOnAction(event -> {
			if(typeLineBox.getValue().equals(lineTypes[0])) {
				premiumBox.setVisible(false);
				cheapBox.setVisible(true);
			}
			else if(typeLineBox.getValue().equals(lineTypes[1])){
				cheapBox.setVisible(false);
				premiumBox.setVisible(true);
			}
		});
	}
    public void setManager(LineMapManager manager) {
    	this.manager=manager;
    }
    @FXML
    public void cancel(ActionEvent event) {
    	((Stage) cancelButton.getScene().getWindow()).close();
    }
    @FXML
    public void next(ActionEvent event) {
    	if(typeLineBox.getValue()==null) {
    		AlertManager.createAlert(AlertType.ERROR, "ERROR", "ERROR: Debe seleccionar un tipo de línea.");
    	}
    	else if(typeLineBox.getValue().equals(lineTypes[0])) { //Economic
    		if(busLine==null)
    			busLine = new CheapLine();
    		else if(busLine.getType()=="Superior")
    			busLine = new CheapLine(busLine.getBusStops(),busLine.getRoutes());
    		if(setValuesCheapLine(busLine)) {
    			setRouteLine();
    		}
    	}
    	else if(typeLineBox.getValue().equals(lineTypes[1])) { //Premium
    		if(busLine==null)
    			busLine = new PremiumLine();
    		else if(busLine.getType()=="Económica")
    			busLine = new PremiumLine(busLine.getBusStops(),busLine.getRoutes());
    		if(setValuesPremiumLine(busLine)) {
    			setRouteLine();
    		}
    	}
    }
    
    public void setRouteLine() {		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/line/LineRouteSelector.fxml"));
			BorderPane root = loader.load();
			LineRouteSelectorController controller = loader.getController();
			controller.setPrevScene(nextButton.getScene());
			controller.setManager(this.manager);
	        Scene scene = new Scene(root);
	        Stage stage = (Stage) nextButton.getScene().getWindow();
	        stage.setScene(scene);
	        controller.init(busLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private Boolean setValuesPremiumLine(BusLine busLine) {
	    try {
	    	if (lineNameField.getText().trim().isEmpty()) {
				throw new EmptyFieldException("Nombre vacio");
			}
	    	busLine.setName(lineNameField.getText().trim());
	    	busLine.setColor(lineColorField.getValue());
	    	busLine.setSeatingCapacity(Integer.parseUnsignedInt(lineSeatingCapacityField.getText().trim()));
			if (!linePremiumWifiBox.isSelected() && !linePremiumAirBox.isSelected()) {
				throw new IncompleteFieldException("Debe seleccionar al menos un servicio");
			}
			if (linePremiumWifiBox.isSelected()) {
				((PremiumLine) busLine).getServices().add(PremiumLineService.WIFI);
			}
			if (linePremiumWifiBox.isSelected()) {
				((PremiumLine) busLine).getServices().add(PremiumLineService.AIR_CONDITIONING);
			}
	    }
	    catch(NumberFormatException e) {
			AlertManager.createAlert(AlertType.ERROR, "Error", "Ingrese números para la cantidad máxima de pasajeros sentados");
			return false;
		}
    	catch(EmptyFieldException|IncompleteFieldException e) {
			AlertManager.createAlert(AlertType.ERROR, "Error", e.getMessage());
			return false;
    	}
	    return true;
    }
    
    private Boolean setValuesCheapLine(BusLine busLine) {
    	try {
    		if (lineNameField.getText().trim().isEmpty()) {
    			throw new EmptyFieldException("Nombre vacio");
    		}
    		busLine.setName(lineNameField.getText().trim());
    		busLine.setColor(lineColorField.getValue());
    		busLine.setSeatingCapacity(Integer.parseUnsignedInt(lineSeatingCapacityField.getText().trim()));
			((CheapLine) busLine).setStandingCapacityPercentage(Double.parseDouble(lineCheapStandingCapacityLabel.getText().replaceAll(",","."))/100);
			//Este no deberia tener errores nunca
			
		}catch(NumberFormatException e) {
			AlertManager.createAlert(AlertType.ERROR, "Error", "Ingrese números para la cantidad máxima de pasajeros sentados");
			return false;
		}
    	catch(EmptyFieldException e) {
			AlertManager.createAlert(AlertType.ERROR, "Error", e.getMessage());
			return false;
    	}
    	return true;
    }
}
