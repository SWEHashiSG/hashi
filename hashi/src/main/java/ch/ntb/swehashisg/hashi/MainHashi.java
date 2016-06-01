package ch.ntb.swehashisg.hashi;

import java.net.URL;

import ch.ntb.swehashisg.hashi.controller.MainWindowController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainHashi extends Application {

	private int windowWidth = 520;
	private int windowHeight = 500;
	public static void main(String[] args) throws Exception {
		launch(args);
	}

	public void start(Stage stage) throws Exception {
		MainWindowController mainWindow = new MainWindowController();
		Scene scene = new Scene(mainWindow,windowHeight, windowHeight);
        scene.getStylesheets().add("/styles/styles.css");
		stage.setTitle("Hashi from Team SWEHashiSG");
		URL url = getClass().getResource("/images/Icon.jpg");
		stage.getIcons().add(new Image(url.toString()));
		stage.setScene(scene);
		stage.show();
		stage.setOnCloseRequest(event -> mainWindow.closeRequest(event));
		stage.setMinWidth(windowWidth);
		stage.setMinHeight(windowHeight);
		System.out.println("min Width: " + mainWindow.getMinWidth());
		System.out.println("max Width: " + mainWindow.getMaxWidth());
		System.out.println("pref Width: " + mainWindow.getPrefWidth());
	}
}