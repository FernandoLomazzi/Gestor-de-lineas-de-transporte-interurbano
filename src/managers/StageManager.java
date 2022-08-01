package managers;

import application.Main;
import javafx.event.EventHandler;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class StageManager {
	private static StageManager manager;
	private Stage mainStage;
	private Double height;
	private Double width;
	private StageManager() {
		mainStage = Main.mainStage;
		height = Screen.getPrimary().getBounds().getHeight()-40;   
		width = Screen.getPrimary().getBounds().getWidth();  
	}
	public static StageManager getInstance() {
		if(manager==null)
			manager = new StageManager();
		return manager;
	}
	public static void updateMainStage() {
		Main.mainStage.setMaximized(false);
		Main.mainStage.setMaximized(true);
	}
	public void configStage(Stage stage) {
	}
}

