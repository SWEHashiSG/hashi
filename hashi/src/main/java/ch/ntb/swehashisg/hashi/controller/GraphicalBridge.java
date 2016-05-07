package ch.ntb.swehashisg.hashi.controller;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;

public class GraphicalBridge {

	//private StackPane stackPane;
	private Line line;

	GraphicalBridge(GridPane gridPane, int startColum, int startRow, int endColum, int endRow) {
		//stackPane = new StackPane();

		if (startColum == endColum) {
			// Vertical
			line = new Line(20, 20, 20, 200);
		} else if (startRow == endRow) {
			// Horizontal
			line = new Line(20, 20, 200, 20);
		} else {
			throw new IllegalArgumentException("Bridges can only be horizontal or vertical");
		}
		gridPane.add(line, startColum, startRow);
	}

//	public void showSignelBridge() {
//		removeBridges();
//		stackPane.getChildren().add(line);
//	}
//
//	public void showDoubleBridge() {
//		removeBridges();
//		stackPane.getChildren().add(line);
//		stackPane.getChildren().add(line);
//		stackPane.getChildren().add(new Label("double"));
//	}
//
//	public void removeBridges() {
//		stackPane.getChildren().clear();
//	}
//
//	public void removePossibleBridge() {
//		removeBridges();
//		stackPane.getChildren().add(line);
//		stackPane.getChildren().add(new Label("Possible"));
//	}
}
