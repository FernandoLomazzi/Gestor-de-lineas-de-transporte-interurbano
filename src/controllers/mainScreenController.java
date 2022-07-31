package controllers;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import managers.CityMapManager;
import managers.LineMapManager;

public class mainScreenController {
	@FXML
	private Button cityMapButton;
	@FXML
	private Button lineScreenButton;
	@FXML
	private Button travelScreenButton;

	private Stage mainStage = Main.mainStage;
	// Event Listener on Button[#cityMapButton].onAction
	@FXML
	public void setCityMap(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/cityMapScreen.fxml"));
			BorderPane cityMapScreen = loader.load();
			cityMapScreenController controller = loader.getController();
			Scene scene = new Scene(cityMapScreen);
			controller.setPrevScene(cityMapButton.getScene());
            mainStage.setScene(scene);
        	CityMapManager cityMapManager = CityMapManager.getInstance();
        	cityMapManager.initView();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	// Event Listener on Button[#lineScreenButton].onAction
	@FXML
	public void setLineScreen(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/views/busLineScreen.fxml"));
			BorderPane busLineScreen = loader.load();
			busLineScreenController controller = loader.getController();
			Scene scene = new Scene(busLineScreen);
			controller.setPrevScene(lineScreenButton.getScene());
			mainStage.setTitle("Menú Lineas");
			mainStage.setScene(scene);
			controller.getLineMapManager().initView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// Event Listener on Button[#travelScreenButton].onAction
	@FXML
	public void setTravelScreen(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/views/busPathScreen.fxml"));
			BorderPane busPathScreen = loader.load();
			busPathScreenController controller = loader.getController();
			Scene scene = new Scene(busPathScreen);
			controller.setPrevScene(travelScreenButton.getScene());
			mainStage.setScene(scene);
			controller.getPathManager().initView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

