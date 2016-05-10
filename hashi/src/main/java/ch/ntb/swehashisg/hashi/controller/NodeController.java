package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;

import ch.ntb.swehashisg.hashi.model.GraphField;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class NodeController extends Pane {
	@FXML
	private Label label;

	private GraphField graphField;

	public NodeController(GraphField graphField) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Node.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		this.graphField = graphField;
		label.textProperty().bind(new SimpleStringProperty(Integer.toString(graphField.getBridges())));
	}

	@FXML
	protected void onMouseClicked() {
		System.out.println("The button was clicked!");
	}

	@FXML
	protected void onMouseEntered() {
		System.out.println("Mouse on Node");
	}

	@FXML
	protected void onMouseExited() {
		System.out.println("Mouse not on Node:-)");
	}
}