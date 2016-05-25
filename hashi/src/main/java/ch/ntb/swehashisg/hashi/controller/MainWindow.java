package ch.ntb.swehashisg.hashi.controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.graph.GraphDas;
import ch.ntb.swehashisg.hashi.graph.GraphDasFactory;
import ch.ntb.swehashisg.hashi.graph.GraphFormat;
import ch.ntb.swehashisg.hashi.graph.Utilities;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.WindowEvent;

public class MainWindow extends AnchorPane {

	private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);

	private static final String XML_DESCRIPTION = "XML File";
	private static final String JSON_DESCRIPTION = "JSon File";

	private GameField gameField;
	GraphDas graphDas;

	@FXML
	private Pane pane;
	@FXML
	private Button buttonUndo;
	@FXML
	private Button butotnRedo;

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
	public void undo() {
		logger.debug("Undo Clicked");
	}

	@FXML
	public void redo() {
		logger.debug("Redo Clicked");
	}

	@FXML
	public void restart() {
		logger.debug("Restart Clicked");
	}

	@FXML
	public void showSolution() {
		logger.debug("Show Solution Clicked");
	}

	@FXML
	public void save() {
		logger.debug("Save Clicked");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Game");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(XML_DESCRIPTION, "*.xml"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(JSON_DESCRIPTION, "*.json"));

		File file = fileChooser.showSaveDialog(this.getScene().getWindow());
		if (file != null) {
			if (fileChooser.getSelectedExtensionFilter().getDescription().equals(XML_DESCRIPTION)) {
				Utilities.persistGraphDas(graphDas, file.getAbsolutePath(), GraphFormat.XML);
			} else if (fileChooser.getSelectedExtensionFilter().getDescription().equals(JSON_DESCRIPTION)) {
				Utilities.persistGraphDas(graphDas, file.getAbsolutePath(), GraphFormat.JSON);
			} else {
				throw new IllegalArgumentException("Unknown File Type");
			}
		}
	}

	@FXML
	public void open() {
		logger.debug("Open Clicked");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Load Game");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(XML_DESCRIPTION, "*.xml"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(JSON_DESCRIPTION, "*.json"));

		File file = fileChooser.showOpenDialog(this.getScene().getWindow());
		if (file != null) {
			if (fileChooser.getSelectedExtensionFilter().getDescription().equals(XML_DESCRIPTION)) {
				graphDas = Utilities.loadGraphDas(file.getAbsolutePath(), GraphFormat.XML);
			} else if (fileChooser.getSelectedExtensionFilter().getDescription().equals(JSON_DESCRIPTION)) {
				graphDas = Utilities.loadGraphDas(file.getAbsolutePath(), GraphFormat.JSON);
			} else {
				throw new IllegalArgumentException("Unknown File Type");
			}
			gameField = new GameField(graphDas);
			gameField.loadGame();
			pane.getChildren().add(gameField);
		} else {
			logger.debug("Open File Dialog Closed without a choosen File");
		}
	}

	@FXML
	public void check() {
		logger.debug("Check Clicked");
	}

	@FXML
	public void clickedOnPane(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.MIDDLE) {
			logger.debug("Starting Editor Mode. For Engineers only ;-)");
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Starting Editor Mode");
			alert.setHeaderText("Welcom Developer. You start now in the Editor Mode");

			ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			ButtonType buttonTypeOK = new ButtonType("OK");
			alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeOK) {
				startEditorMode(12);
			}
		}
	}

	private void startEditorMode(int gameSize) {
		graphDas = GraphDasFactory.getEmptyGraphDas(gameSize);
		gameField = new GameField(graphDas);
		gameField.loadGame();
		pane.getChildren().add(gameField);
	}

	@FXML
	public void loadGame() {
		logger.debug("Started");
		graphDas = GraphDasFactory.getGraphDas();
		gameField = new GameField(graphDas);
		gameField.loadGame();
		pane.getChildren().add(gameField);
	}

	@FXML
	public void addBridge() {
		logger.debug("Add Bridge");
	}

	@FXML
	public void addDoubleBridge() {
		logger.debug("Add Double Bridge Test");
	}

	@FXML
	public void showHint() {
		logger.debug("Show Hint");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.showOpenDialog(this.getScene().getWindow());
	}

	@FXML
	public void removeHint() {
		logger.debug("Remove Hint");
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
			save();
		} else if (result.get() == buttonTypeCloseWithoutSave) {

		}
	}

}
