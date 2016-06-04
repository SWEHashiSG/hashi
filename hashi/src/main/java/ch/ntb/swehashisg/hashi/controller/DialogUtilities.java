package ch.ntb.swehashisg.hashi.controller;

import java.io.File;

import ch.ntb.swehashisg.hashi.graph.GraphFormat;
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
}
