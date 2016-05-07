package ch.ntb.swehashisg.hashi.controller;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;

class GraphicalNode{

	private final int CIRCLE_RADIUS = 20;
	private Font numberFont = new Font("System",24);
	
	private Circle circle;
	private Label label;
	
	GraphicalNode(int connections) {
		circle = new Circle();
		circle.setRadius(CIRCLE_RADIUS);
		circle.setFill(Color.WHITE);
		circle.setStroke(Color.BLACK);
		circle.setStrokeWidth(2);
		circle.setStrokeType(StrokeType.OUTSIDE);
		label = new Label("" + connections);
		label.setFont(numberFont);
		label.setPrefSize(40, 40);
		label.setAlignment(Pos.CENTER);
	}
	
	public void addToGrid(GridPane gridPane, int colum, int row){
		StackPane layout = new StackPane();
		layout.getChildren().addAll(circle,label);
		gridPane.add(layout, colum, row);
	}
	
	public void dissable(){
		circle.setDisable(true);
		label.setDisable(true);
	}
	
	public void enable(){
		circle.setDisable(false);
		label.setDisable(false);
	}
}
