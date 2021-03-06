package ch.ntb.swehashisg.hashi.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ch.ntb.swehashisg.hashi.graph.GraphFormat;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class DialogUtilities {

	/**
	 * Description type for XML and JSon files used for the open- and save-file
	 * dialog.
	 */
	private static final String XML_DESCRIPTION = "XML File";
	private static final String JSON_DESCRIPTION = "JSon File";

	public static GraphPersistence selectSaveGraph(String title, Window window) {
		return selectGraph(true, title, window);
	}

	public static GraphPersistence selectOpenGraph(String title, Window window) {
		return selectGraph(false, title, window);
	}

	/**
	 * Open a File Select dialog to save or load a file
	 * 
	 * @param save
	 *            if true save file if false load file
	 * @param title
	 *            title of file dialog
	 * @param window
	 *            window where this dialog will be attached
	 * @return GraphPersistence with file informations
	 */
	private static GraphPersistence selectGraph(boolean save, String title, Window window) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(XML_DESCRIPTION, "*.xml"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(JSON_DESCRIPTION, "*.json"));
		File file = null;
		if (save) {
			file = fileChooser.showSaveDialog(window);
		} else {
			file = fileChooser.showOpenDialog(window);
		}

		if (file != null) {
			if (fileChooser.getSelectedExtensionFilter().getDescription().equals(XML_DESCRIPTION)) {
				return new GraphPersistence(file.getAbsolutePath(), GraphFormat.XML);
			} else if (fileChooser.getSelectedExtensionFilter().getDescription().equals(JSON_DESCRIPTION)) {
				return new GraphPersistence(file.getAbsolutePath(), GraphFormat.XML);
			} else {
				throw new IllegalArgumentException("Unknown File Type");
			}
		} else {
			return null;
		}
	}

	/**
	 * Shows Alert Window after user has started a check.
	 * 
	 * @param correct
	 *            if chack hasn't found a fault
	 */
	public static void showCheckAlter(boolean correct) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Game Check");
		if (correct) {
			alert.setHeaderText("Your on the right way");
		} else {
			alert.setHeaderText("Sorry, but there are some errors in your Game");
		}
		alert.showAndWait();
	}

	/**
	 * Shows editor Start Dialogs for user to choose desired game size to create
	 * a new Game
	 * 
	 * @return Dimension2D for X and Y game size
	 */
	public static Dimension2D showEditorModeDialog() {
		List<Integer> choices = new ArrayList<>();
		choices.add(6);
		choices.add(8);
		choices.add(10);
		choices.add(12);
		choices.add(14);
		ChoiceDialog<Integer> dialog = new ChoiceDialog<Integer>(8, choices);
		dialog.setTitle("Starting Editor Mode");
		dialog.setHeaderText("Please select your desired Gamesize");
		dialog.setContentText("Choose Width");

		Optional<Integer> resultWidth = dialog.showAndWait();
		if (resultWidth.isPresent()) {
			int width = resultWidth.get();
			dialog.setContentText("Choos Height");

			Optional<Integer> resultHeight = dialog.showAndWait();
			if (resultHeight.isPresent()) {
				int heigth = resultHeight.get();
				return new Dimension2D(width, heigth);
			}
		}
		return null;
	}

	/**
	 * open a help window for the user for more informations
	 */
	public static void showHelpDialog() {
		Stage stage = new Stage();
		HelpWindowController mainWindow = new HelpWindowController();
		Scene scene = new Scene(mainWindow, 300, 300);
		scene.getStylesheets().add("/styles/styles.css");
		stage.setTitle("Hashi from Team SWEHashiSG");
		stage.getIcons().add(new Image("/images/Icon.jpg"));
		stage.setScene(scene);
		stage.show();
		stage.setResizable(false);
	}
}
