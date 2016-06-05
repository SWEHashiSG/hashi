package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.graph.GraphDas;
import ch.ntb.swehashisg.hashi.graph.GraphDasFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Dimension2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.WindowEvent;

/**
 * Controller class for the main window of the game. All buttons are allocated
 * on this pane and also the game field. This controller handles all the user
 * Inputs from the buttons and also keyboard inputs.
 * 
 * @author Martin
 */
public class MainWindowController extends AnchorPane {

	private static final Logger logger = LoggerFactory.getLogger(MainWindowController.class);

	/**
	 * Controller of the game field which will be placed on this panel.
	 */
	private GameFieldController gameField;

	/**
	 * Data model of the game
	 */
	GraphDas graphDas;

	/**
	 * JavaFX Attributes from the FXML-File. pane where the game field will be
	 * placed.
	 */
	@FXML
	private Pane pane;

	/**
	 * JavaFX Attributes from the FXML-File. All buttons on the game field. Used
	 * to enable and disable if the representing function is not available.
	 */
	@FXML
	private Button buttonUndo;
	@FXML
	private Button buttonRedo;
	@FXML
	private Button buttonSave;
	@FXML
	private Button buttonOpen;
	@FXML
	private Button buttonShowSolution;
	@FXML
	private Button buttonCheck;
	@FXML
	private Button buttonRestart;
	@FXML
	private Button buttonHelp;

	/**
	 * Constructor of the MainWindowController. Loads the FXML-file and also
	 * initialize the buttons.
	 */
	public MainWindowController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			error(exception);
			throw new RuntimeException(exception);
		}
		updateButtons(false, false);
	}

	/**
	 * User input. Called from GUI if the user clicks on the undo button.
	 */
	@FXML
	public void undo() {
		try {
			gameField.undo();
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * User input. Called from GUI if the user clicks on the redo button.
	 */
	@FXML
	public void redo() {
		try {
			gameField.redo();
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * User input. Called from GUI if the user clicks on the restart button.
	 */
	@FXML
	public void restart() {
		try {
			gameField.restart();
			if (gameField.isShowingSolution()) {
				gameField.showSolution();
			}
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * User input. Called from GUI if the user clicks on the show solution
	 * button.
	 */
	@FXML
	public void showSolution() {
		try {
			gameField.showSolution();
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * User input. Called from GUI if the user clicks on the check button.
	 */
	@FXML
	public void check() {
		try {
			DialogUtilities.showCheckAlter(gameField.isCorrect());
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * User input. Called from GUI if the user clicks on the help button.
	 */
	@FXML
	public void help() {
		try {
			DialogUtilities.showHelpDialog();
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * User input. Called from GUI if the user clicks on the save button. Open a
	 * save file dialog were he can choose the designation and the file type.
	 * Supports XML and JSon
	 */
	@FXML
	public void save() {
		try {
			if (graphDas == null) {
				return;
			}
			logger.debug("Save Clicked");
			GraphPersistence graphPersistence = DialogUtilities.selectSaveGraph("Save Game",
					this.getScene().getWindow());
			if (graphPersistence != null) {
				GraphDasFactory.persistGraphDas(graphDas, graphPersistence);
			} else {
				logger.debug("Open File Dialog Closed without a choosen File");
			}
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * User input. Called from GUI if the user clicks on the open button. Open a
	 * file open dialog were he can choose which file he would load. Supports
	 * XML and JSon If the user does not aboard the choosen game loads.
	 */
	@FXML
	public void open() {
		try {
			logger.debug("Open Clicked");
			GraphPersistence graphPersistence = DialogUtilities.selectOpenGraph("Load Game",
					this.getScene().getWindow());
			if (graphPersistence != null) {
				GraphDas newGraphDas = GraphDasFactory.loadGraphDas(graphPersistence);
				graphDas = newGraphDas;
				gameField = new GameFieldPlayController(graphDas, this);
				addGameField(gameField);
				gameField.initiateUpdate();
			} else {
				logger.debug("Open File Dialog Closed without a choosen File");
			}
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * adds the game field on this main window.
	 * 
	 * @param gameField
	 *            to add on the main window
	 */
	private void addGameField(GameFieldController gameField) {
		pane.getChildren().clear();
		pane.getChildren().add(gameField);
		updateButtons(graphDas);
	}

	/**
	 * User input if the user clicks on the empty screen on the pane. Hidden
	 * function to start the game editor mode. Only starts if user clicks with
	 * mouse wheel on this pane. Shows user a dialog to choose the game size and
	 * starts editor mode.
	 * 
	 * @param mouseEvent
	 */
	@FXML
	public void clickedOnPane(MouseEvent mouseEvent) {
		try {
			if (mouseEvent.getButton() == MouseButton.MIDDLE) {
				Dimension2D dim = DialogUtilities.showEditorModeDialog();
				if (dim != null) {
					startEditorMode((int) dim.getWidth(), (int) dim.getHeight());
				}
			}
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * starts the editor mode. Creating new empty game with sizeX and sizeY.
	 * show the empty game on this window.
	 * 
	 * @param sizeX
	 *            horizontal size of new game
	 * @param sizeY
	 *            vertical size of new game
	 */
	private void startEditorMode(int sizeX, int sizeY) {
		graphDas = GraphDasFactory.getEmptyGraphDas(sizeX, sizeY);
		GameFieldDesignerController gameField = new GameFieldDesignerController(graphDas, this);
		addGameField(gameField);
		gameField.initiateUpdate();
	}

	/**
	 * User input when he try to close the window. if a game is loaded show him
	 * a dialog to choose between aboard, save game or quit without saving.
	 * 
	 * @param event
	 *            WindowEvent from stage
	 */
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
			GraphDasFactory.closeGraphDas(graphDas);
			Platform.exit();
		} else if (result.get() == buttonTypeCloseWithoutSave) {
			GraphDasFactory.closeGraphDas(graphDas);
			Platform.exit();
		}
	}

	/**
	 * Update all buttons on the main window. Called every time if something has
	 * changed on the model.
	 * 
	 * @param graphDas
	 */
	void updateButtons(GraphDas graphDas) {
		if (graphDas != null) {
			updateButtons(graphDas.canUndo(), graphDas.canRedo());
		}
	}

	/**
	 * Update all buttons on the main window. Called every time if something has
	 * changed on the model.
	 * 
	 * @param graphDas
	 */
	private void updateButtons(boolean canUndo, boolean canRedo) {
		buttonUndo.setDisable(!canUndo);
		buttonRedo.setDisable(!canRedo);
		buttonShowSolution.setDisable(graphDas == null);
		buttonRestart.setDisable(graphDas == null);
		buttonSave.setDisable(graphDas == null);
		buttonCheck.setDisable(graphDas == null);
	}

	/**
	 * User input if the user type something on the keyboard during this window
	 * is in front. Some standard Shortcuts to save, open, undo and redo.
	 * 
	 * @param event
	 *            KeyEvent form the keyboard.
	 */
	@FXML
	public void onKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.S & event.isControlDown()) {
			logger.debug("CTRL + S pressed");
			save();
		} else if (event.getCode() == KeyCode.Z & event.isControlDown()) {
			logger.debug("CTRL + Z pressed");
			undo();
		} else if (event.getCode() == KeyCode.Y & event.isControlDown()) {
			logger.debug("CTRL + Y pressed");
			redo();
		} else if (event.getCode() == KeyCode.O & event.isControlDown()) {
			logger.debug("CTRL + O pressed");
			open();
		} else if (event.getCode() == KeyCode.F1) {
			logger.debug("F1 pressed");
			help();
		}
	}

	public void error(Exception ex) {
		logger.error(ex.getMessage(), ex);
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Application fault");
		alert.setHeaderText("Sorry but there was an error in this application!");
		alert.setContentText(ex.getMessage());
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		printWriter.flush();
		String exceptionText = writer.toString();
		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);
		alert.showAndWait();
	}
}
