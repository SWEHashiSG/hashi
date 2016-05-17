package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;
import java.util.ArrayList;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class Field extends StackPane {
	@FXML
	private Label label;
	@FXML
	private Circle highliter;

	private GraphField graphField;

	public Field(GraphField graphField) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Field.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		this.graphField = graphField;
		label.textProperty().bind(new SimpleStringProperty(Integer.toString(graphField.getBridges())));
		// highliter.visibleProperty().bind(new SimpleBooleanProperty(false));
		// TODO: Bind to GraphField
	}

	public void addToGameField(GameField gameField) {
		gameField.add(this, graphField.getX(), graphField.getY());
	}

	@FXML
	protected void onMouseClicked() {
		System.out.println("Clicked on Node!");
	}

	@FXML
	protected void onMouseEntered() {
		System.out.println("Mouse on Node");
		highliter.setVisible(true);
	}

	@FXML
	protected void onMouseExited() {
		System.out.println("Mouse not on Node:-)");
		highliter.setVisible(false);
	}

	private ArrayList<GraphBridge> getPossibleBridges() {
		ArrayList<GraphBridge> bridges = new ArrayList<>();
		for (GraphField field : graphField.getNeighbors()) {
			bridges.add(new GraphBridge(graphField, field));
		}
		return bridges;
	}
}