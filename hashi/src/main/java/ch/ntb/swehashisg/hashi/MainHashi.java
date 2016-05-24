package ch.ntb.swehashisg.hashi;

import java.net.URL;

import ch.ntb.swehashisg.hashi.controller.MainWindow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainHashi extends Application {

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	public void start(Stage stage) throws Exception {
		MainWindow mainWindow = new MainWindow();
		Scene scene = new Scene(mainWindow, 500, 500);
        scene.getStylesheets().add("/styles/styles.css");
		stage.setTitle("Hashi from Team SWEHashiSG");
		URL url = getClass().getResource("/images/Icon.jpg");
		stage.getIcons().add(new Image(url.toString()));
		stage.setScene(scene);
		stage.show();
		stage.setOnCloseRequest(event -> mainWindow.closeRequest(event));
	}
}