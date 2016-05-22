package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;

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
	private GameField gameField;
	private static int fieldSize = 40;

	public GraphField getGraphField() {
		return graphField;
	}

	public void setGraphField(GraphField graphField) {
		this.graphField = graphField;
	}

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
		label.setText(Integer.toString(graphField.getBridges()));
	}

	public void addToGameField() {
		gameField.add(this, graphField.getX(), graphField.getY());
	}

	@FXML
	protected void onMouseClicked() {
		System.out.println("Clicked on Field! X=" + graphField.getX() + "  Y=" + graphField.getY());
		// No Function implemented when clicking on Field
	}

	@FXML
	protected void onMouseEntered() {
		System.out.println("Mouse on Field! X=" + graphField.getX() + "  Y=" + graphField.getY());
		highliter.setVisible(true);
		setHighliterFromBridges(true);

	}

	@FXML
	protected void onMouseExited(MouseEvent event) {
		highliter.setVisible(false);
		setHighliterFromBridges(false);
		Bridge bridge = null;
		if (event.getX() <= 0 && event.getY() > fieldSize / 3 && event.getY() < fieldSize * 2 / 3) {
			System.out.println(" mouse leave Field to west");
			bridge = gameField.getWestBrigde(this);
			if (bridge != null) {
				bridge.setHighlite(true);
			}
		} else if (event.getX() >= fieldSize && event.getY() > fieldSize / 3 && event.getY() < fieldSize * 2 / 3) {
			System.out.println(" mouse leave Field to east");
			bridge = gameField.getEastBridge(this);
			if (bridge != null) {
				bridge.setHighlite(true);
			}
		} else if (event.getY() <= 0 && event.getX() > fieldSize / 3 && event.getX() < fieldSize * 2 / 3) {
			System.out.println(" mouse leave Field to north");
			bridge = gameField.getNorthBridge(this);
			if (bridge != null) {
				bridge.setHighlite(true);
			}
		} else if (event.getY() >= fieldSize && event.getX() > fieldSize / 3 && event.getX() < fieldSize * 2 / 3) {
			System.out.println(" mouse leave Field to south");
			bridge = gameField.getSouthBridge(this);
			if (bridge != null) {
				bridge.setHighlite(true);
			}
		}
	}

	private void setHighliterFromBridges(boolean highlited) {
		Bridge bridge = gameField.getNorthBridge(this);
		if (bridge != null) {
			bridge.setHighlite(highlited);
		}
		bridge = gameField.getEastBridge(this);
		if (bridge != null) {
			bridge.setHighlite(highlited);
		}
		bridge = gameField.getSouthBridge(this);
		if (bridge != null) {
			bridge.setHighlite(highlited);
		}
		bridge = gameField.getWestBrigde(this);
		if (bridge != null) {
			bridge.setHighlite(highlited);
		}
	}
}