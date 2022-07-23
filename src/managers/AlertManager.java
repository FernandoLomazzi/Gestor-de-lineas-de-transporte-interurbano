package managers;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertManager {
	private AlertManager() {
		;
	}
	public static void createAlert(AlertType alertType,String title,String contentText) {
		Alert alert = new Alert(alertType);
	    alert.setHeaderText(null);
	    alert.setTitle(title);
	    alert.setContentText(contentText);
	    alert.showAndWait();
	}
}
