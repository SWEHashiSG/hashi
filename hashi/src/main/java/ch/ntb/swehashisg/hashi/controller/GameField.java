package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;
import java.util.Set;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class GameField extends GridPane {

	public GameField(int gameSize) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GameField.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		setFieldSize(gameSize);
		setGridLinesVisible(true);					// For debugging show grid lines
	}

	private void setFieldSize(int gameSize) {
		getRowConstraints().clear();
		for (int i = 0; i < gameSize; i++) {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPercentHeight(100.0 / gameSize);			//Automatic Size of Window 	
			//rowConstraints.setPrefHeight(40);							// Fix Size of Window
			getRowConstraints().add(rowConstraints);
			ColumnConstraints columnConstraints = new ColumnConstraints();
			columnConstraints.setPercentWidth(100.0 / gameSize);		//Automatic Size of Window 
			//columnConstraints.setPrefWidth(40);							// Fixe Size of Window
			getColumnConstraints().add(columnConstraints);
		}

	}

	public void loadGame(Set<GraphField> graphFields) {
		for (GraphField graphField : graphFields) {
			new Field(graphField).addToGameField(this);
		}
	}

	public void addBridge(GraphBridge graphBridge) {
		new Bridge(graphBridge).addToGameField(this);;
	}
}
