package ch.ntb.swehashisg.hashi.controller;

import java.util.ArrayList;

import ch.ntb.swehashisg.hashi.model.FieldModel;

import ch.ntb.swehashisg.hashi.xml.XMLHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;

public class Controller {

	private FieldModel fieldModel;
	private XMLHandler xmlHandler;
	private int gameSize = 10;
	private ArrayList<GraphicalNode> graphicalNodes = new ArrayList<GraphicalNode>();
	private ArrayList<GraphicalBridge> graphicalBrides = new ArrayList<GraphicalBridge>();

	@FXML
	private Pane gameFieldPane;
	@FXML
	private Button buttonTest;

	@FXML
	public void buttonClicked(ActionEvent event) {

		System.out.println("Started");
		buttonTest.setDisable(true);
		buttonTest.setText("Game is Started");
		initCanvas(gameSize);
		addNode(0, 0, 3);
		addNode(0, 5, 1);
		addNode(5, 5, 8);
		addNode(8, 0, 5);
		addNode(8, 5, 6);
	}
	
	@FXML
	public void addBridge(ActionEvent event){
		System.out.println("Add Bridge Test");
		addBridge(graphicalNodes.get(0), graphicalNodes.get(1));
	}
	
	@FXML
	public void addDoubleBridge(ActionEvent event){
		System.out.println("Add Double Bridge Test");
		addDoubleBridge(graphicalNodes.get(3), graphicalNodes.get(4));
	}
	
	@FXML
	public void showHint(ActionEvent event){
		System.out.println("Show Hint");
		graphicalBrides.get(1).drawHintLine();
	}
	
	@FXML
	public void removeHint(ActionEvent event){
		System.out.println("Remove Hint");
		graphicalBrides.get(1).removeHintLine();
	}

	private void initCanvas(int gameSize) {
		int gameFieldSize = gameSize*GraphicalNode.CIRCLE_DIAMETER;
		gameFieldPane.setPrefSize(gameFieldSize, gameFieldSize);
		cleanPane();
	}

	private void addNode(int colum, int row, int value) {
		GraphicalNode node = new GraphicalNode(colum, row, value);
		graphicalNodes.add(node);
		node.draw(gameFieldPane);
	}

	private void addBridge(GraphicalNode graphicalNode1, GraphicalNode graphicalNode2) {
		GraphicalBridge bridge = new GraphicalBridge(gameFieldPane, graphicalNode1,graphicalNode2);
		graphicalBrides.add(bridge);
		bridge.drawSingleLine();
	}
	
	private void addDoubleBridge(GraphicalNode graphicalNode1, GraphicalNode graphicalNode2){
		GraphicalBridge bridge = new GraphicalBridge(gameFieldPane, graphicalNode1,graphicalNode2);
		graphicalBrides.add(bridge);
		bridge.drawDoubleLine();
	}

	private void cleanPane() {
		gameFieldPane.getChildren().clear();
	}
}
