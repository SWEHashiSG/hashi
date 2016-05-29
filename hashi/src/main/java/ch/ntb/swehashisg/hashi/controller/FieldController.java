package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.model.GraphField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class FieldController extends StackPane {

	private static Logger logger = LoggerFactory.getLogger(FieldController.class);

	@FXML
	private Label label;
	@FXML
	private Circle highliter;

	private GraphField graphField;
	private GameFieldController gameField;
	private static int fieldSize = 40;

	public GraphField getGraphField() {
		return graphField;
	}

	public void setGraphField(GraphField graphField) {
		this.graphField = graphField;
	}

	public FieldController(GraphField graphField, GameFieldController gameField) {
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
		if (graphField.getExistingBridges().size() == graphField.getBridges()) {
			setDisable(true);
		}
	}

	public void addToGameField() {
		gameField.add(this, graphField.getX(), graphField.getY());
	}

	@FXML
	protected void onMouseClicked() {
		logger.debug("Clicked on Field! X=" + graphField.getX() + "  Y=" + graphField.getY());
		gameField.clickedOnField(this);
	}

	@FXML
	protected void onMouseEntered() {
		logger.debug("Mouse on Field! X=" + graphField.getX() + "  Y=" + graphField.getY());
		highliter.setVisible(true);
		setHighliterFromBridges(true);

	}

	@FXML
	protected void onMouseExited(MouseEvent event) {
		highliter.setVisible(false);
		setHighliterFromBridges(false);
		if (isMouseWest(event) && graphField.hasWestNeighbor()) {
			logger.debug(" mouse leave Field to west");
			HighlightController bridge = gameField.getWestHighlight(this);
			if (gameField.needsBridge(bridge)) {
				bridge.setHighlite(true);
			}
		} else if (isMouseEast(event) && graphField.hasEastNeighbor()) {
			logger.debug(" mouse leave Field to east");
			HighlightController bridge = gameField.getEastHighlight(this);
			if (gameField.needsBridge(bridge)) {
				bridge.setHighlite(true);
			}
		} else if (isMouseNorth(event) && graphField.hasNorthNeighbor()) {
			logger.debug(" mouse leave Field to north");
			HighlightController bridge = gameField.getNorthHighlight(this);
			if (gameField.needsBridge(bridge)) {
				bridge.setHighlite(true);
			}
		} else if (isMouseSouth(event) && graphField.hasSouthNeighbor()) {
			logger.debug(" mouse leave Field to south");
			HighlightController bridge = gameField.getSouthHighlight(this);
			if (gameField.needsBridge(bridge)) {
				bridge.setHighlite(true);
			}
		}
	}

	private boolean isMouseSouth(MouseEvent event) {
		return event.getY() >= fieldSize && event.getX() > fieldSize / 3 && event.getX() < fieldSize * 2 / 3;
	}

	private boolean isMouseNorth(MouseEvent event) {
		return event.getY() <= 0 && event.getX() > fieldSize / 3 && event.getX() < fieldSize * 2 / 3;
	}

	private boolean isMouseEast(MouseEvent event) {
		return event.getX() >= fieldSize && event.getY() > fieldSize / 3 && event.getY() < fieldSize * 2 / 3;
	}

	private boolean isMouseWest(MouseEvent event) {
		return event.getX() <= 0 && event.getY() > fieldSize / 3 && event.getY() < fieldSize * 2 / 3;
	}

	private void setHighliterFromBridges(boolean highlited) {
		if (graphField.hasWestNeighbor()) {
			HighlightController bridge = gameField.getWestHighlight(this);
			if (gameField.needsBridge(bridge)) {
				bridge.setHighlite(highlited);
			}
		}
		if (graphField.hasEastNeighbor()) {
			HighlightController bridge = gameField.getEastHighlight(this);
			if (gameField.needsBridge(bridge)) {
				bridge.setHighlite(highlited);
			}
		}
		if (graphField.hasNorthNeighbor()) {
			HighlightController bridge = gameField.getNorthHighlight(this);
			if (gameField.needsBridge(bridge)) {
				bridge.setHighlite(highlited);
			}
		}
		if (graphField.hasSouthNeighbor()) {
			HighlightController bridge = gameField.getSouthHighlight(this);
			if (gameField.needsBridge(bridge)) {
				bridge.setHighlite(highlited);
			}
		}
	}

	public static int getFieldSize() {
		return fieldSize;
	}
}