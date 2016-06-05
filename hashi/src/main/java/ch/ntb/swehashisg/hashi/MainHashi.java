package ch.ntb.swehashisg.hashi;

import java.net.URL;

import ch.ntb.swehashisg.hashi.controller.MainWindowController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main class from the Hashi game. Starts the application and show it on the screen.
 * @author Martin
 *
 */
public class MainHashi extends Application {

	/**
	 * Window width and height which is needed for a good optic
	 */
	private int windowWidth = 580;
	private int windowHeight = 500;
	
	/**
	 * Main class which is called at the Program start
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		launch(args);
	}

	/**
	 * Start class called from the JavaFX.
	 * Create a new Stage and add the main window.
	 */
	public void start(Stage stage) throws Exception {
		MainWindowController mainWindow = new MainWindowController();
		Scene scene = new Scene(mainWindow,windowWidth, windowHeight);
        scene.getStylesheets().add("/styles/styles.css");
		stage.setTitle("Hashi from Team SWEHashiSG");
		URL url = getClass().getResource("/images/Icon.jpg");
		stage.getIcons().add(new Image(url.toString()));
		stage.setScene(scene);
		stage.show();
		stage.setOnCloseRequest(event -> mainWindow.closeRequest(event));
		stage.setMinWidth(windowWidth);
		stage.setMinHeight(windowHeight);
	}	
}