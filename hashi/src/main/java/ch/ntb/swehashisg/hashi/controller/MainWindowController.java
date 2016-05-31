package ch.ntb.swehashisg.hashi.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import javafx.scene.control.ChoiceDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;

public class MainWindowController extends AnchorPane {

	private static final Logger logger = LoggerFactory.getLogger(MainWindowController.class);

	private static final String XML_DESCRIPTION = "XML File";
	private static final String JSON_DESCRIPTION = "JSon File";

	private GameFieldController gameField;
	GraphDas graphDas;

	@FXML
	private Pane pane;
	@FXML
	private Button buttonUndo;
	@FXML
	private Button buttonRedo;

	public MainWindowController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		updateButtons(false, false);
	}

	@FXML
	public void undo() {
		gameField.undo();
	}

	@FXML
	public void redo() {
		gameField.redo();
	}

	@FXML
	public void restart() {
		gameField.restart();
	}

	@FXML
	public void showSolution() {
		gameField.showSolution();
	}

	@FXML
	public void check() {
		logger.debug("Check Clicked");
	}

	@FXML
	public void save() {
		if (graphDas == null) {
			return;
		}
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
			gameField = new GameFieldPlayController(graphDas, this);
			gameField.loadGame();
			addGameField(gameField);
		} else {
			logger.debug("Open File Dialog Closed without a choosen File");
		}
	}

	private void addGameField(GameFieldController gameField) {
		pane.getChildren().add(gameField);
		updateButtons(graphDas);
	}

	@FXML
	public void clickedOnPane(MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.MIDDLE) {
			List<Integer> choices = new ArrayList<>();
			choices.add(6);
			choices.add(8);
			choices.add(10);
			choices.add(12);
			choices.add(14);
			ChoiceDialog<Integer> dialog = new ChoiceDialog<Integer>(8, choices);
			dialog.setTitle("Starting Editor Mode");
			dialog.setHeaderText("Pleas select your desired Gamesize");
			dialog.setContentText("Choos Width");

			Optional<Integer> resultWidth = dialog.showAndWait();
			if (resultWidth.isPresent()) {
				int width = resultWidth.get();
				dialog.setContentText("Choos Height");

				Optional<Integer> resultHeight = dialog.showAndWait();
				if (resultHeight.isPresent()) {
					int heigth = resultHeight.get();
					startEditorMode(width, heigth);
				}
			}
		}
	}

	private void startEditorMode(int sizeX, int sizeY) {
		graphDas = GraphDasFactory.getEmptyGraphDas(sizeX, sizeY);
		GameFieldDesignerController gameField = new GameFieldDesignerController(graphDas, this);
		gameField.loadGame();
		addGameField(gameField);
	}

	public void closeRequest(WindowEvent event) {
		if (graphDas == null) {
			return;
		}
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
			// simple close Window
		}
	}
	
	void updateButtons(GraphDas graphDas){
		if (graphDas != null){
			updateButtons(graphDas.canUndo(), graphDas.canRedo());
		}
	}

	private void updateButtons(boolean canUndo, boolean canRedo) {
		buttonUndo.setDisable(!canUndo);
		buttonRedo.setDisable(!canRedo);
	}

	public void onKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.S & event.isControlDown()) {
			logger.debug("CTRL + S pressed");
			save();
		} else if (event.getCode() == KeyCode.Z & event.isControlDown()) {
			logger.debug("CTRL + Z pressed");
			undo();
		} else if (event.getCode() == KeyCode.V & event.isControlDown()) {
			logger.debug("CTRL + Y pressed");
			redo();
		} else if (event.getCode() == KeyCode.O & event.isControlDown()) {
			logger.debug("CTRL + O pressed");
			open();
		}
	}
}
