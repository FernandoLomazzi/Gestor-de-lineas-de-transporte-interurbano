package managers;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

public class AlertManager {
	private AlertManager() {
		;
	}
	public static Alert createAlert(AlertType alertType,String title,String contentText) {
		Alert alert = new Alert(alertType);
	    alert.setHeaderText(null);
	    alert.setTitle(title);
	    alert.initStyle(StageStyle.UTILITY);
	    alert.getDialogPane().setStyle("-fx-font: normal 10pt verdana;");
	    alert.setContentText(contentText);
	    return alert;
	}
}
