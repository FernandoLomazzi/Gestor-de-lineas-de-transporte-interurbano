module TPINTEGRADOR {
	requires javafx.controls;
	requires java.sql;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires java.desktop;
	
	opens application to javafx.graphics, javafx.fxml;
	opens controllers to javafx.fxml;
	opens controllers.line to javafx.fxml;
	opens models.busline to javafx.fxml, javafx.base;
	opens models to javafx.base;
	opens controllers.route to  javafx.fxml;
	opens controllers.stop to javafx.fxml;
	opens controllers.incident to javafx.fxml;
}
