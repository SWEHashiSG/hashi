package ch.ntb.swehashisg.hashi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.graph.GraphDas;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;

public class GameFieldPlayController extends GameFieldController {

	private static final Logger logger = LoggerFactory.getLogger(GameFieldPlayController.class);
	
	private GameTime gameTime;

	public GameFieldPlayController(GraphDas graphDas) {
		super(graphDas);

		gameTime = new GameTime();
	}

//	private static class UpdateThread extends Thread {
//
//		private GameFieldController gameField;
//		private GraphDas graphDas;
//
//		public UpdateThread(GameFieldController gameField, GraphDas graphDas) {
//			this.gameField = gameField;
//			this.graphDas = graphDas;
//		}
//
//		@Override
//		public void run() {
//			GraphPlayField playField = graphDas.getPlayField();
//			Platform.runLater(new Runnable() {
//
//				@Override
//				public void run() {
//					gameField.update(playField);
//				}
//			});
//
//		}
//	}

	/**
	 * Creat all Bridges with zero weighting for GUI
	 */
	private void createAllBridgesHighlights(Set<GraphBridge> bridges) {
		graphBridgeToBridge = new HashMap<>();
		graphBridgeToHighlight = new HashMap<>();
		for (GraphBridge bridge : bridges) {
			graphBridgeToBridge.put(bridge, new BridgeController(bridge, this));
		}
		Map<GraphField, GraphField> lightFieldToRealField = new HashMap<>();
		for (GraphField field : graphFields) {
			lightFieldToRealField.put(field, field);
		}
		for (GraphField field : graphFields) {
			for (GraphField neighbor : field.getNeighbors()) {
				GraphField fullNeighbor = lightFieldToRealField.get(neighbor);
				GraphBridge newBridge = new GraphBridge(field, fullNeighbor);

				if (!bridges.contains(newBridge)) {
					graphBridgeToBridge.put(newBridge, new BridgeController(newBridge, this));
				}
				graphBridgeToHighlight.put(newBridge, new HighlightController(field, fullNeighbor, this));
			}
		}
	}

	protected void update(GraphPlayField graphPlayField) {
		super.update(graphPlayField);
		if (graphDas.isCorrect()) {
			finishGame();
		}
	}

	private void setFieldSize(int sizeX, int sizeY) {
		getRowConstraints().clear();
		getColumnConstraints().clear();
		for (int i = 0; i < sizeY; i++) {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPercentHeight(100.0 / sizeY);
			getRowConstraints().add(rowConstraints);

		}
		for (int i = 0; i < sizeX; i++) {
			ColumnConstraints columnConstraints = new ColumnConstraints();
			columnConstraints.setPercentWidth(100.0 / sizeX);
			getColumnConstraints().add(columnConstraints);
		}
	}

	public void loadGame() {
		logger.debug("Draw all Fields");
		cleanGameField();
		fields = new ArrayList<FieldController>();
		for (GraphField graphField : graphFields) {
			if (graphField.getX() == 0 && graphField.getY() == 1) {
				logger.debug("Mööp");
			}
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
	
	@FXML
	private void onMouseClicked(){
		// Nothing to do in Play Mode
	}
}
