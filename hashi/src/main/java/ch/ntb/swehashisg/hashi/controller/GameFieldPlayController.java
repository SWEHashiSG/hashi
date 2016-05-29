package ch.ntb.swehashisg.hashi.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.graph.GraphDas;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class GameFieldPlayController extends GameFieldController {

	private static final Logger logger = LoggerFactory.getLogger(GameFieldPlayController.class);

	private GameTime gameTime;

	public GameFieldPlayController(GraphDas graphDas) {
		super(graphDas);
		gameTime = new GameTime();
	}


	protected void update(GraphPlayField graphPlayField) {
		super.update(graphPlayField);
		if (graphDas.isCorrect()) {
			finishGame();
		}
	}

	public void loadGame() {
		logger.debug("Draw all Fields");
		cleanGameField();
		fields = new ArrayList<FieldController>();
		for (GraphField graphField : graphFields) {
			FieldController field = new FieldController(graphField, this);
			field.addToGameField();
			fields.add(field);

		}
		for (HighlightController highlight : graphBridgeToHighlight.values()) {
			highlight.addToGameField();
		}
		for (BridgeController bridge : graphBridgeToBridge.values()) {
			bridge.addToGameField();
		}
	}

	protected void cleanGameField() {
		this.getChildren().clear();
	}

	protected HighlightController getNorthHighlight(FieldController field) {
		return graphBridgeToHighlight
				.get(new GraphBridge(field.getGraphField(), field.getGraphField().getNorthNeighbor()));
	}

	protected HighlightController getEastHighlight(FieldController field) {
		return graphBridgeToHighlight
				.get(new GraphBridge(field.getGraphField(), field.getGraphField().getEastNeighbor()));
	}

	protected HighlightController getSouthHighlight(FieldController field) {
		return graphBridgeToHighlight
				.get(new GraphBridge(field.getGraphField(), field.getGraphField().getSouthNeighbor()));
	}

	protected HighlightController getWestHighlight(FieldController field) {
		return graphBridgeToHighlight
				.get(new GraphBridge(field.getGraphField(), field.getGraphField().getWestNeighbor()));
	}

	public boolean hasBridge(GraphField neighbor1, GraphField neighbor2) {
		return graphBridgeToBridge.containsKey(new GraphBridge(neighbor1, neighbor2));
	}

	public boolean needsBridge(HighlightController highlight) {
		GraphBridge bridge = new GraphBridge(highlight.getNeighbor1(), highlight.getNeighbor2());
		int weighting = graphBridgeToBridge.get(bridge).getGraphBridge().getWeighting();
		if (highlight.getNeighbor1().getBridges() > highlight.getNeighbor1().getExistingBridges().size()
				&& highlight.getNeighbor2().getBridges() > highlight.getNeighbor2().getExistingBridges().size()) {
			if (weighting < 2) {
				return true;
			}
		}
		return false;
	}

	public void removeBridge(HighlightController highlight) {
		if (!isUpdating) {
			GraphBridge bridge = new GraphBridge(highlight.getNeighbor1(), highlight.getNeighbor2());
			if (graphBridgeToBridge.get(bridge).getGraphBridge().getWeighting() == 2) {
				graphDas.removeBridge(bridge);
			}
			graphDas.removeBridge(bridge);
			logger.debug("-------------Redraw whole gamefield-----------------");
			UpdateThread updateThread = new UpdateThread(this, graphDas);
			isUpdating = true;
			updateThread.run();
		} else {
			logger.warn("Is already updating, better solution needed!");
		}
	}

	public void addBridge(HighlightController highlight) {
		if (!gameTime.isRunning()) {
			gameTime.startTime();
		}
		super.addBridge(highlight);
	}

	private void finishGame() {
		gameTime.stopTime();
		logger.info("Game is Finished");
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Finish GAme");
		alert.setHeaderText("Congratulation. You have finished the Hashi Game");
		alert.setContentText("Time: " + gameTime.getTime() + " Seconds");
		alert.showAndWait();
	}

	@Override
	void onMouseClicked(MouseEvent event) {
		// Nothing to do in Play Mode
	}


	@Override
	void clickedOnField(FieldController field) {
		// TODO Auto-generated method stub
	}
}
