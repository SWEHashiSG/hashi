package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;
import java.util.Optional;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.graph.GraphDas;
import ch.ntb.swehashisg.hashi.graph.GraphDasFactory;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;

public class MainWindow extends AnchorPane {
	
	private static final Logger log = LoggerFactory.getLogger(MainWindow.class);

	private GameField gameField;
	GraphDas graphDas;
	private int gameSize = 8; // TODO: Get form GameDas

	@FXML
	private Pane pane;

	public MainWindow() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	@FXML
	public void undo(){
		
	}
	
	@FXML
	public void redo(){
		
	}
	
	@FXML
	public void restart(){
		
	}
	
	@FXML
	public void showSolution(){
		
	}
	
	@FXML
	public void save(){
		
	}
	
	@FXML
	public void loadGame() {
		log.debug("Started");
		graphDas = GraphDasFactory.getGraphDas();
		gameField = new GameField(graphDas);
		gameField.loadGame();
		pane.getChildren().add(gameField);
	}

	@FXML
	public void addBridge() {
		log.debug("Add Bridge");
	}

	@FXML
	public void addDoubleBridge() {
		log.debug("Add Double Bridge Test");
	}

	@FXML
	public void showHint() {
		log.debug("Show Hint");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.showOpenDialog(this.getScene().getWindow());
	}

	@FXML
	public void removeHint() {
		log.debug("Remove Hint");
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Information Dialog");
		alert.setHeaderText("Look, an Information Dialog");
		alert.setContentText("I have a great message for you!");
		alert.showAndWait();
	}

	public void closeRequest(WindowEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Saving game?");
		alert.setHeaderText("Do you want to save your game before exit?");

		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		ButtonType buttonTypeSave = new ButtonType("Save");
		ButtonType buttonTypeCloseWithoutSave = new ButtonType("Close without saving");
		alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeCloseWithoutSave, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeCancel) {
			event.consume();
		} else if (result.get() == buttonTypeSave) {
			// TODO: Save Dialog
			throw new NotImplementedException();
		} else if (result.get() == buttonTypeCloseWithoutSave) {

		}
	}

}
