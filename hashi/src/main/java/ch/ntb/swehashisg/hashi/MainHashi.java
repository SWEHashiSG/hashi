package ch.ntb.swehashisg.hashi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainHashi extends Application {

    @FXML private GridPane gridPane;
    @FXML private Circle testCircle;
    
    private int gameSize = 10;
    private static final int CIRCLE_RADIUS = 22;
    
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage stage) throws Exception {

        String fxmlFile = "/fxml/mainWindow.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));

        Scene scene = new Scene(rootNode, 500, 500);

        stage.setTitle("Hashi from Team SWEHashiSG");
        stage.setScene(scene);
        stage.show();
        stage.setMinWidth(CIRCLE_RADIUS*2*gameSize);
        stage.setMinHeight(CIRCLE_RADIUS*2*gameSize+20);
    }
}