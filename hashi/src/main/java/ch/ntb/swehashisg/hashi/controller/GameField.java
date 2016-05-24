package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;
import java.util.ArrayList;

import ch.ntb.swehashisg.hashi.graph.GraphDas;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class GameField extends GridPane {

	private GraphDas graphDas;
	private ArrayList<GraphField> graphFields;
	private ArrayList<Field> fields;
	private ArrayList<GraphBridge> graphBridges;
	private ArrayList<Bridge> bridges;

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
		graphFields = new ArrayList<GraphField>();
		graphFields.addAll(graphDas.getRelevantFields());
		graphBridges = new ArrayList<GraphBridge>();
		createAllBridges();
	}

	/**
	 * Creat all Bridges with zero weighting for GUI
	 */
	private void createAllBridges() {
		for (GraphField field : graphFields) {
			for (GraphField neighbor : field.getNeighbors()) {
				GraphBridge newBridge = new GraphBridge(field, neighbor);
				if (!graphBridges.contains(newBridge)) {
					graphBridges.add(newBridge);
				}
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
		System.out.println("Draw all Fields");
		cleanGameField();
		fields = new ArrayList<Field>();
		bridges = new ArrayList<Bridge>();
		for (GraphField graphField : graphFields) {
			Field field = new Field(graphField, this);
			field.addToGameField();
			fields.add(field);

		}
		for (GraphBridge graphBridge : graphBridges) {
			Bridge bridge = new Bridge(graphBridge, this);
			bridge.addToGameField();
			bridges.add(bridge);
		}
	}

	public void addBridge(GraphBridge graphBridge) {
		graphDas.addBridge(graphBridge);
		System.out.println("-------------Redraw whole gamefield-----------------");
		loadGame(); // TODO: Update
	}

	protected void cleanGameField() {
		this.getChildren().clear();
	}
	

	protected Bridge getNorthBridge(Field field) {
		for (Bridge bridge : bridges) {
			if (bridge.getGraphBridge().getField1().equals(field.getGraphField())){
				if (bridge.getGraphBridge().getField2().getY() < field.getGraphField().getY()){
					return bridge;
				}
			} else if(bridge.getGraphBridge().getField2().equals(field.getGraphField())){
				if (bridge.getGraphBridge().getField1().getY() < field.getGraphField().getY()){
					return bridge;
				}
			}
		}
		return null;
	}

	protected Bridge getEastBridge(Field field) {
		for (Bridge bridge : bridges) {
			if (bridge.getGraphBridge().getField1().equals(field.getGraphField())){
				if (bridge.getGraphBridge().getField2().getX() > field.getGraphField().getX()){
					return bridge;
				}
			} else if(bridge.getGraphBridge().getField2().equals(field.getGraphField())){
				if (bridge.getGraphBridge().getField1().getX() > field.getGraphField().getX()){
					return bridge;
				}
			}
		}
		return null;
	}

	protected Bridge getSouthBridge(Field field) {
		for (Bridge bridge : bridges) {
			if (bridge.getGraphBridge().getField1().equals(field.getGraphField())){
				if (bridge.getGraphBridge().getField2().getY() > field.getGraphField().getY()){
					return bridge;
				}
			} else if(bridge.getGraphBridge().getField2().equals(field.getGraphField())){
				if (bridge.getGraphBridge().getField1().getY() > field.getGraphField().getY()){
					return bridge;
				}
			}
		}
		return null;
	}

	protected Bridge getWestBrigde(Field field) {
		for (Bridge bridge : bridges) {
			if (bridge.getGraphBridge().getField1().equals(field.getGraphField())){
				if (bridge.getGraphBridge().getField2().getX() < field.getGraphField().getX()){
					return bridge;
				}
			} else if(bridge.getGraphBridge().getField2().equals(field.getGraphField())){
				if (bridge.getGraphBridge().getField1().getX() < field.getGraphField().getX()){
					return bridge;
				}
			}
		}
		return null;
	}
}
