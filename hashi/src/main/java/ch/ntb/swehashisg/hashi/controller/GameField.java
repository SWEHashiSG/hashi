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
	private ArrayList<GraphField> fieldsOnGui;
	private ArrayList<GraphBridge> bridgesOnGui;

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
		fieldsOnGui = new ArrayList<GraphField>();
		bridgesOnGui = new ArrayList<GraphBridge>();
		setFieldSize(8); // TODO: get Game Size from GraphDas
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
		System.out.println("Redraw complete Gui");
		cleanGameField();
		for (GraphField graphField : graphDas.getRelevantFields()) {
			new Field(graphField, this).addToGameField();
			fieldsOnGui.add(graphField);
			for (GraphBridge existingBridge : graphField.getExistingBridges()){
				if (!bridgesOnGui.contains(existingBridge)){
					new Bridge(existingBridge,this,1).addToGameField();
				} else {
				}
			}
		}
	}

	public void addBridge(GraphBridge graphBridge) {
		graphDas.addBridge(graphBridge);
		//new Bridge(graphBridge,this).addToGameField();
		loadGame();			// redraw Gui
	}
	
	private void cleanGameField(){
		fieldsOnGui.clear();
		bridgesOnGui.clear();
		this.getChildren().clear();
	}
}
