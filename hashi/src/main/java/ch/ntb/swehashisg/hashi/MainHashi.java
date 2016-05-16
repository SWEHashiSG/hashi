package ch.ntb.swehashisg.hashi;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import ch.ntb.swehashisg.hashi.controller.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class MainHashi extends Application {

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	public void start(Stage stage) throws Exception {
		MainWindow mainWindow = new MainWindow();
		Scene scene = new Scene(mainWindow, 500, 500);

		stage.setTitle("Hashi from Team SWEHashiSG");
		URL url = getClass().getResource("/images/Icon.jpg");
		stage.getIcons().add(new Image(url.toString()));
		stage.setScene(scene);
		stage.show();
		stage.setOnCloseRequest(event -> mainWindow.closeRequest(event));
	}
}