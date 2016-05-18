package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;
import java.util.ArrayList;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class Field extends StackPane {
	@FXML
	private Label label;
	@FXML
	private Circle highliter;

	private GraphField graphField;
	private ArrayList<Bridge> possibleBridges;
	private GameField gameField;

	private Bridge leftBridge;
	private Bridge rightBridge;
	private Bridge upperBridge;
	private Bridge underBridge;

	public Field(GraphField graphField, GameField gameField) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Field.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		this.graphField = graphField;
		this.gameField = gameField;
		possibleBridges = new ArrayList<Bridge>();
		label.setText(Integer.toString(graphField.getBridges()));
		highliter.setVisible(false);
	}

	public void addToGameField() {
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
		clearPossibleBridges();
		for (GraphField neighbor : graphField.getNeighbors()) {
			Bridge bridge = new Bridge(new GraphBridge(graphField, neighbor),gameField,0);
			possibleBridges.add(bridge);
			bridge.highlite();
			bridge.addToGameField();
		}
	}

	@FXML
	protected void onMouseExited(MouseEvent event) {
		highliter.setVisible(false);
		if (event.getX() <= 0) {
			// mouse leave node to left
			System.out.println("left");
		} else if (event.getX() >= highliter.getRadius() * 2) {
			// mouse leave node to right
			System.out.println("right");
		} else if (event.getY() <= 0) {
			// mouse leave node to upper
			System.out.println("upper");
		} else if (event.getY() >= highliter.getRadius() * 2) {
			// mouse leave node to down
			System.out.println("down");
		}
		// clearPossibleBridges();
	}

	private void clearPossibleBridges() {
		for (Bridge bridge : possibleBridges) {
			gameField.getChildren().remove(bridge);
		}
		possibleBridges.clear();
	}

	private void highliteAllBridges() {
		if (leftBridge != null) {
			leftBridge.highlite();
		}
		if (rightBridge != null) {
			rightBridge.highlite();
		}
		if (upperBridge != null) {
			upperBridge.highlite();
		}
		if (underBridge != null) {
			underBridge.highlite();
		}
	}
}