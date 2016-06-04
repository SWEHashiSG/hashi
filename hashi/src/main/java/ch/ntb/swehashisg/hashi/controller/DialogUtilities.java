package ch.ntb.swehashisg.hashi.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ch.ntb.swehashisg.hashi.graph.GraphFormat;
import javafx.geometry.Dimension2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.stage.FileChooser;
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

	public static void showHelpDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Hashi");
		alert.setHeaderText("About this Programm");
		String text = "More information about this Program: \n" + "www.github.com/swehashisg";
		alert.setContentText(text);
		alert.showAndWait();
	}
}
