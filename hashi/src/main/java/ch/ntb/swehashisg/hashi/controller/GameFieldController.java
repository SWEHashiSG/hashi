package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;
import java.util.Set;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class GameFieldController extends GridPane {

	public GameFieldController(int gameSize) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GameField.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		setFieldSize(gameSize);
	}

	private void setFieldSize(int gameSize) {
		getRowConstraints().clear();
		for (int i = 0; i < gameSize; i++) {
			RowConstraints rowConstraints = new RowConstraints();
			//rowConstraints.setPercentHeight(100.0 / gameSize);
			rowConstraints.setPrefHeight(40);
			getRowConstraints().add(rowConstraints);
			ColumnConstraints columnConstraints = new ColumnConstraints();
			//columnConstraints.setPercentWidth(100.0 / gameSize);
			columnConstraints.setPrefWidth(40);
			getColumnConstraints().add(columnConstraints);
		}

	}

	public void loadGame(Set<GraphField> graphFields) {
		for (GraphField graphField : graphFields) {
			new NodeController(graphField).addToGameField(this);
		}
	}

	public void addBridge(GraphBridge graphBridge) {
		new BridgeController(graphBridge).addToGameField(this);;
	}
}
