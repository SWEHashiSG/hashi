package ch.ntb.swehashisg.hashi.controller;

import java.util.ArrayList;
import java.util.Iterator;

import ch.ntb.swehashisg.hashi.graph.GraphDas;
import ch.ntb.swehashisg.hashi.graph.GraphDasFactory;
import ch.ntb.swehashisg.hashi.model.FieldModel;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.xml.XMLHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.event.ActionEvent;

public class Controller {

	private GameFieldController gameFieldController;
	private XMLHandler xmlHandler;
	GraphDas graphDas;

	@FXML
	private Pane pane;
	@FXML
	private Button buttonTest;

	@FXML
	public void buttonClicked(ActionEvent event) {
		System.out.println("Started");
		buttonTest.setDisable(true);
		buttonTest.setText("Game is Started");
		//initCanvas(gameSize);
		loadGame();
	}

	private void loadGame() {
		gameFieldController = new GameFieldController();
		pane.getChildren().add(gameFieldController);
		graphDas = GraphDasFactory.getGraphDas();
		gameFieldController.loadGame(graphDas.getRelevantFields(),pane);
	}

	@FXML
	public void addBridge(ActionEvent event) {
		System.out.println("Add Bridge Test");
		GraphBridge bridge = new GraphBridge(graphDas.getRelevantFields().iterator().next(), graphDas.getRelevantFields().iterator().next());
		gameFieldController.addBridge(bridge);
	}

	@FXML
	public void addDoubleBridge(ActionEvent event) {
		System.out.println("Add Double Bridge Test");
	}

	@FXML
	public void showHint(ActionEvent event) {
		System.out.println("Show Hint");
	}

	@FXML
	public void removeHint(ActionEvent event) {
		System.out.println("Remove Hint");
	}
}
