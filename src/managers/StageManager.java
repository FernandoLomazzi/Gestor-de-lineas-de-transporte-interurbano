package managers;

import application.Main;
import javafx.event.EventHandler;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class StageManager {
	public static void updateMainStage() {
		Main.mainStage.setMaximized(false);
		Main.mainStage.setMaximized(true);
	}
}
