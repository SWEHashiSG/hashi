package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.graph.GraphDas;
import ch.ntb.swehashisg.hashi.graph.BaseGraphDas;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class GameField extends GridPane {

	private static final Logger logger = LoggerFactory.getLogger(GameField.class);

	private GraphDas graphDas;
	private Set<GraphField> graphFields;
	private ArrayList<Field> fields;
	private HashMap<GraphBridge, Bridge> graphBridgeToBridge;
	private HashMap<GraphBridge, Highlight> graphBridgeToHighlight;
	private GameTime gameTime;

	public GameField(GraphDas graphDas) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GameField.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		this.graphDas = graphDas;
		setFieldSize(8); // TODO: get Game Size from GraphDas
		GraphPlayField graphPlayField = graphDas.getPlayField();
		graphFields = graphPlayField.getFields();
		createAllBridgesHighlights(graphPlayField.getBridges());
		gameTime = new GameTime();
	}

	/**
	 * Creat all Bridges with zero weighting for GUI
	 */
	private void createAllBridgesHighlights(Set<GraphBridge> bridges) {
		graphBridgeToBridge = new HashMap<>();
		graphBridgeToHighlight = new HashMap<>();
		for (GraphBridge bridge : bridges) {
			graphBridgeToBridge.put(bridge, new Bridge(bridge, this));
		}
		for (GraphField field : graphFields) {
			for (GraphField neighbor : field.getNeighbors()) {
				GraphBridge newBridge = new GraphBridge(field, neighbor);

				if (!bridges.contains(newBridge)) {
					graphBridgeToBridge.put(newBridge, new Bridge(newBridge, this));
				}
				graphBridgeToHighlight.put(newBridge, new Highlight(field, neighbor, this));
			}
		}
	}

	private void setFieldSize(int gameSize) {
		getRowConstraints().clear();
		getColumnConstraints().clear();
		for (int i = 0; i < gameSize; i++) {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPercentHeight(100.0 / gameSize);
			getRowConstraints().add(rowConstraints);
			ColumnConstraints columnConstraints = new ColumnConstraints();
			columnConstraints.setPercentWidth(100.0 / gameSize);
			getColumnConstraints().add(columnConstraints);
		}
	}

	public void loadGame() {
		logger.debug("Draw all Fields");
		cleanGameField();
		fields = new ArrayList<Field>();
		for (GraphField graphField : graphFields) {
			Field field = new Field(graphField, this);
			field.addToGameField();
			fields.add(field);

		}
		for (Highlight highlight : graphBridgeToHighlight.values()) {
			highlight.addToGameField();
		}
		for (Bridge bridge : graphBridgeToBridge.values()) {
			bridge.addToGameField();
		}
	}

	protected void cleanGameField() {
		this.getChildren().clear();
	}

	protected Highlight getNorthHighlight(Field field) {
		return graphBridgeToHighlight
				.get(new GraphBridge(field.getGraphField(), field.getGraphField().getNorthNeighbor()));
	}

	protected Highlight getEastHighlight(Field field) {
		return graphBridgeToHighlight
				.get(new GraphBridge(field.getGraphField(), field.getGraphField().getEastNeighbor()));
	}

	protected Highlight getSouthHighlight(Field field) {
		return graphBridgeToHighlight
				.get(new GraphBridge(field.getGraphField(), field.getGraphField().getSouthNeighbor()));
	}

	protected Highlight getWestHighlight(Field field) {
		return graphBridgeToHighlight
				.get(new GraphBridge(field.getGraphField(), field.getGraphField().getWestNeighbor()));
	}

	public boolean hasBridge(GraphField neighbor1, GraphField neighbor2) {
		return graphBridgeToBridge.containsKey(new GraphBridge(neighbor1, neighbor2));
	}

	public boolean needsBridge(Highlight highlight) {
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

	public void removeBridge(Highlight highlight) {
		GraphBridge bridge = new GraphBridge(highlight.getNeighbor1(), highlight.getNeighbor2());
		if (graphBridgeToBridge.get(bridge).getGraphBridge().getWeighting() == 2) {
			graphDas.removeBridge(bridge);
		}
		graphDas.removeBridge(bridge);
		logger.debug("-------------Redraw whole gamefield-----------------");
		GraphPlayField graphPlayField = graphDas.getPlayField();
		graphFields = graphPlayField.getFields();
		createAllBridgesHighlights(graphPlayField.getBridges());
		loadGame(); // TODO: Update
	}

	public void addBridge(Highlight highlight) {
		if (!gameTime.isRunning()){
			gameTime.startTime();
		}
		GraphBridge bridge = new GraphBridge(highlight.getNeighbor1(), highlight.getNeighbor2());
		graphDas.addBridge(bridge);
		logger.debug("-------------Redraw whole gamefield-----------------");
		GraphPlayField graphPlayField = graphDas.getPlayField();
		graphFields = graphPlayField.getFields();
		createAllBridgesHighlights(graphPlayField.getBridges());
		loadGame(); // TODO: Update
	}
}
