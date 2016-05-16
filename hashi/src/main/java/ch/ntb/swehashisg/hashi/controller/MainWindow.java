package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;
import java.util.Optional;

import org.apache.commons.lang.NotImplementedException;

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

	private GameField gameField;
	GraphDas graphDas;
	private int gameSize = 8; // Todo: Get form GameDas

	@FXML
	private Pane pane;
	@FXML
	private Button buttonTest;

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
	public void buttonClicked(ActionEvent event) {
		System.out.println("Started");
		buttonTest.setDisable(true);
		buttonTest.setText("Game is Started");
		loadGame();
	}

	private void loadGame() {
		gameField = new GameField(gameSize);
		pane.getChildren().add(gameField);
		graphDas = GraphDasFactory.getGraphDas();
		gameField.loadGame(graphDas.getRelevantFields());
	}

	@FXML
	public void addBridge(ActionEvent event) {
		System.out.println("Add Bridge");
		gameField.addBridge(new GraphBridge(new GraphField(0, 1, 1), new GraphField(7, 1, 1)));
		gameField.addBridge(new GraphBridge(new GraphField(6, 2, 1), new GraphField(6, 5, 2)));
	}

	@FXML
	public void addDoubleBridge(ActionEvent event) {
		System.out.println("Add Double Bridge Test");
	}

	@FXML
	public void showHint(ActionEvent event) {
		System.out.println("Show Hint");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.showOpenDialog(this.getScene().getWindow());
	}

	@FXML
	public void removeHint(ActionEvent event) {
		System.out.println("Remove Hint");
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
