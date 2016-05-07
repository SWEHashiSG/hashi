package ch.ntb.swehashisg.hashi.controller;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class GraphicalBridge {

	private final static int OFFSET = GraphicalNode.CIRCLE_DIAMETER / 2;
	private final static int DISTANZE = 4;
	private final static int HINT_LINE_WIDTH = 15;

	private GraphicalNode graphicalNode1;
	private GraphicalNode graphicalNode2;
	private Pane pane;
	private Line line1;
	private Line line2;
	private Line lineHint;
	private BridgeDirection bridgeDirection;
	int startX;
	int startY;
	int endX;
	int endY;

	GraphicalBridge(Pane pane, GraphicalNode graphicalNode1, GraphicalNode graphicalNode2) {
		this.pane = pane;
		this.graphicalNode1 = graphicalNode1;
		this.graphicalNode2 = graphicalNode2;
		if (graphicalNode1.getColum() == graphicalNode2.getColum()) {
			bridgeDirection = BridgeDirection.Vertical;
		} else if (graphicalNode1.getRow() == graphicalNode2.getRow()) {
			bridgeDirection = BridgeDirection.Horizontal;
		} else {
			throw new IllegalArgumentException("Only horizontal or vertical bridges are allowed");
		}
		startX = graphicalNode1.getColum() * GraphicalNode.CIRCLE_DIAMETER + OFFSET;
		startY = graphicalNode1.getRow() * GraphicalNode.CIRCLE_DIAMETER + OFFSET;
		endX = graphicalNode2.getColum() * GraphicalNode.CIRCLE_DIAMETER + OFFSET;
		endY = graphicalNode2.getRow() * GraphicalNode.CIRCLE_DIAMETER + OFFSET;
	}

	public void drawSingleLine() {
		clean();
		line1 = new Line(startX, startY, endX, endY);
		line2 = null;
		pane.getChildren().add(0, line1); // Index as 0 so it is in Background
	}

	public void drawDoubleLine() {
		clean();
		if (bridgeDirection == BridgeDirection.Vertical) {
			line1 = new Line(startX + DISTANZE, startY, endX + DISTANZE, endY);
			line2 = new Line(startX - DISTANZE, startY, endX - DISTANZE, endY);
		} else if (bridgeDirection == BridgeDirection.Horizontal) {
			line1 = new Line(startX, startY + DISTANZE, endX, endY + DISTANZE);
			line2 = new Line(startX, startY - DISTANZE, endX, endY - DISTANZE);
		}
		pane.getChildren().add(0, line1); // Index as 0 so it is in Background
		pane.getChildren().add(0, line2);
	}
	
	public void drawHintLine(){
		//clean();
		lineHint = new Line(startX, startY, endX, endY);
		lineHint.strokeWidthProperty().set(HINT_LINE_WIDTH);
		lineHint.setStroke(Color.ORANGE);
		pane.getChildren().add(0,lineHint);
		
	}
	
	public void removeHintLine(){
		clean();
		lineHint = null;
		pane.getChildren().remove(lineHint);
	}

	private void clean() {
		pane.getChildren().remove(line1);
		pane.getChildren().remove(line2);
	}

}

enum BridgeDirection {
	Horizontal, Vertical
}
