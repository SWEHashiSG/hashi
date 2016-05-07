package ch.ntb.swehashisg.hashi.controller;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;

class GraphicalNode{

	public final static int CIRCLE_DIAMETER = 40;
	private final static Font numberFont = new Font("System",24);
	
	private Circle circle;
	private Label label;
	private int colum;
	private int row;
	
	GraphicalNode(int colum, int row, int connections) {
		this.colum = colum;
		this.row = row;
		circle = new Circle();
		circle.setRadius(CIRCLE_DIAMETER/2);
		circle.setFill(Color.WHITE);
		circle.setStroke(Color.BLACK);
		circle.setStrokeWidth(2);
		circle.setStrokeType(StrokeType.INSIDE);
		circle.setLayoutX(colum*CIRCLE_DIAMETER + CIRCLE_DIAMETER/2);
		circle.setLayoutY(row*CIRCLE_DIAMETER + CIRCLE_DIAMETER/2);
		label = new Label("" + connections);
		label.setFont(numberFont);
		label.setPrefSize(40, 40);
		label.setLayoutX(colum*CIRCLE_DIAMETER + CIRCLE_DIAMETER/3);
		label.setLayoutY(row*CIRCLE_DIAMETER);
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

	public void draw(Pane pane) {
		pane.getChildren().add(circle);
		pane.getChildren().add(label);
	}
	
	public int getColum(){
		return colum;
	}
	
	public int getRow(){
		return row;
	}
}
