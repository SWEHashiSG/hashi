package ch.ntb.swehashisg.hashi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainHashi extends Application {

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	public void start(Stage stage) throws Exception {

		String fxmlFile = "/fxml/MainWindow.fxml";
		FXMLLoader loader = new FXMLLoader();
		Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));

		Scene scene = new Scene(rootNode, 500, 500);

		stage.setTitle("Hashi from Team SWEHashiSG");
		stage.setScene(scene);
		stage.show();
	}
}