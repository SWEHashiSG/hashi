package ch.ntb.swehashisg.hashi.controller;

import ch.ntb.swehashisg.hashi.model.FieldModel;

import ch.ntb.swehashisg.hashi.xml.XMLHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.event.ActionEvent;

public class Controller {

	private FieldModel fieldModel;
	private XMLHandler xmlHandler;

	private int gameSize = 10;

	@FXML
	private GridPane gridPane;
	@FXML
	private Button buttonTest;

	@FXML
	public void buttonClicked(ActionEvent event) {

		System.out.println("Started");
		buttonTest.setDisable(true);
		buttonTest.setText("Game is Started");
		initGridPane(gameSize);
		addNode(0, 0, 3);
		addNode(0, 5, 1);
		addNode(5, 5, 8);
		addNode(8, 0, 5);
		addNode(8, 5, 6);

		addBridge(5, 0, 5, 8);
		addBridge(0, 0, 0, 8);
		addBridge(0, 8, 5, 8);
		addBridge(5, 5, 5, 8);
	}

	private void initGridPane(int gameSize) {
		cleanGrid();
		gridPane.setGridLinesVisible(true); // for Debugging

		for (int i = 0; i < gameSize; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setMinWidth(40);
			colConst.setPrefWidth(100);
			gridPane.getColumnConstraints().add(colConst);
		}
		for (int i = 0; i < gameSize; i++) {
			RowConstraints rowConst = new RowConstraints();
			rowConst.setMinHeight(40);
			rowConst.setPrefHeight(100);
			gridPane.getRowConstraints().add(rowConst);
		}

	}

	private void addNode(int colum, int row, int value) {
		GraphicalNode node = new GraphicalNode(value);
		node.addToGrid(gridPane, colum, row);
	}

	private void addBridge(int beginRow, int beginColum, int endRow, int endColum) {
		GraphicalBridge graphicalBridge = new GraphicalBridge(gridPane, beginColum, beginRow, endColum, endRow);
		// graphicalBridge.showSignelBridge();
	}

	private void cleanGrid() {
		gridPane.getChildren().clear();
		gridPane.getColumnConstraints().clear();
		gridPane.getRowConstraints().clear();
	}
}
