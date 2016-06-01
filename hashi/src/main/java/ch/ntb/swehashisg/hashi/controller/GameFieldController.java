package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.graph.GraphDas;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public abstract class GameFieldController extends GridPane {

	private static final Logger logger = LoggerFactory.getLogger(GameFieldController.class);

	private MainWindowController mainWindowController;
	protected GraphDas graphDas;
	protected Set<GraphField> graphFields;
	protected ArrayList<FieldController> fields;
	protected HashMap<GraphBridge, BridgeController> graphBridgeToBridge;
	protected HashMap<GraphBridge, HighlightController> graphBridgeToHighlight;
	protected HashMap<GraphBridge, BridgeController> graphBridgeToSolutionBridge;
	protected boolean isUpdating = false;

	public GameFieldController(GraphDas graphDas, MainWindowController mainWindowController) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GameField.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		this.graphDas = graphDas;
		this.mainWindowController = mainWindowController;
		setFieldSize(graphDas.getSizeX(), graphDas.getSizeY());
		GraphPlayField graphPlayField = graphDas.getPlayField();
		graphFields = graphPlayField.getFields();
		createAllBridges(getBridges(graphPlayField));
		createAllSolutions(graphPlayField.getSolutionBridges());
	}

	private void createAllSolutions(Set<GraphBridge> solutionBridges) {
		graphBridgeToSolutionBridge = new HashMap<>();
		for (GraphBridge bridge : solutionBridges) {
			graphBridgeToSolutionBridge.put(bridge, new BridgeController(bridge, this, false));
		}
	}

	protected abstract Set<GraphBridge> getBridges(GraphPlayField graphPlayField);

	private void createAllBridges(Set<GraphBridge> bridges) {
		graphBridgeToBridge = new HashMap<>();
		for (GraphBridge bridge : bridges) {
			graphBridgeToBridge.put(bridge, new BridgeController(bridge, this));
		}
		HashMap<GraphField, GraphField> lightFieldToRealField = new HashMap<>();
		for (GraphField field : graphFields) {
			lightFieldToRealField.put(field, field);
		}
		graphBridgeToHighlight = new HashMap<>();
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
		graphFields = graphPlayField.getFields();
		createAllBridges(getBridges(graphPlayField));
		loadGame();
		isUpdating = false;
	}

	private void setFieldSize(int sizeX, int sizeY) {
		getRowConstraints().clear();
		getColumnConstraints().clear();
		for (int i = 0; i < sizeY; i++) {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPrefHeight(FieldController.getFieldSize());
			getRowConstraints().add(rowConstraints);
		}
		for (int i = 0; i < sizeX; i++) {
			ColumnConstraints columnConstraints = new ColumnConstraints();
			columnConstraints.setPrefWidth(FieldController.getFieldSize());
			getColumnConstraints().add(columnConstraints);
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
		for (BridgeController solution : graphBridgeToSolutionBridge.values()) {
			solution.addToGameField();
		}
	}

	private void cleanGameField() {
		if (!getChildren().isEmpty()) {
			Node n = getChildren().get(0);
			getChildren().clear();
			if (n instanceof Group) {
				logger.debug("Grid Lines are Visible");
				getChildren().add(n);
			}
		}
	}

	HighlightController getNorthHighlight(FieldController field) {
		return graphBridgeToHighlight
				.get(new GraphBridge(field.getGraphField(), field.getGraphField().getNorthNeighbor()));
	}

	HighlightController getEastHighlight(FieldController field) {
		return graphBridgeToHighlight
				.get(new GraphBridge(field.getGraphField(), field.getGraphField().getEastNeighbor()));
	}

	HighlightController getSouthHighlight(FieldController field) {
		return graphBridgeToHighlight
				.get(new GraphBridge(field.getGraphField(), field.getGraphField().getSouthNeighbor()));
	}

	HighlightController getWestHighlight(FieldController field) {
		return graphBridgeToHighlight
				.get(new GraphBridge(field.getGraphField(), field.getGraphField().getWestNeighbor()));
	}

	boolean hasBridge(GraphField neighbor1, GraphField neighbor2) {
		return graphBridgeToBridge.containsKey(new GraphBridge(neighbor1, neighbor2));
	}

	GraphBridge getBridge(GraphField neighbor1, GraphField neighbor2) {
		return graphBridgeToBridge.get(new GraphBridge(neighbor1, neighbor2)).getGraphBridge();
	}

	boolean needsBridge(HighlightController highlight) {
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
				removeBridge(bridge);
			}
			removeBridge(bridge);
			initiateUpdate();
		} else {
			logger.warn("Is already updating, better solution needed!");
		}
	}

	public void addBridge(HighlightController highlight) {
		if (!isUpdating) {
			GraphBridge bridge = new GraphBridge(highlight.getNeighbor1(), highlight.getNeighbor2());
			addBridge(bridge);
			initiateUpdate();
		} else {
			logger.warn("Is already updating, better solution needed!");
		}
	}

	protected abstract void addBridge(GraphBridge graphBridge);

	protected abstract void removeBridge(GraphBridge graphBridge);

	@FXML
	abstract void onMouseClicked(MouseEvent event);

	protected void clickedOnField(FieldController field) {
		// Do nothing
	}

	protected void initiateUpdate() {
		logger.debug("-------------Redraw whole gamefield-----------------");
		UpdateThread updateThread = new UpdateThread(this, graphDas, mainWindowController);
		isUpdating = true;
		updateThread.run();
	}

	static class UpdateThread extends Thread {

		private GameFieldController gameField;
		private GraphDas graphDas;
		private MainWindowController mainWindowController;

		public UpdateThread(GameFieldController gameField, GraphDas graphDas,
				MainWindowController mainWindowController) {
			this.gameField = gameField;
			this.graphDas = graphDas;
			this.mainWindowController = mainWindowController;
		}

		@Override
		public void run() {
			GraphPlayField playField = graphDas.getPlayField();
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					mainWindowController.updateButtons(graphDas);
					gameField.update(playField);
				}
			});
		}
	}

	public void showSolution() {
		for (BridgeController solutionBridge : graphBridgeToSolutionBridge.values()) {
			solutionBridge.toggleVisibility();
		}
		for (BridgeController bridge : graphBridgeToBridge.values()) {
			bridge.toggleVisibility();
		}
	}

	public void undo() {
		if (graphDas.canUndo()) {
			graphDas.undo();
			initiateUpdate();
		}
	}

	public void redo() {
		if (graphDas.canRedo()) {
			graphDas.redo();
			initiateUpdate();
		}
	}

	public void restart() {
		graphDas.restart();
		initiateUpdate();
	}
}
