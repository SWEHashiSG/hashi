package ch.ntb.swehashisg.hashi.controller;

import ch.ntb.swehashisg.hashi.graph.GraphDas;
import ch.ntb.swehashisg.hashi.graph.GraphDasFactory;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class MainWindowController {

	private GameFieldController gameFieldController;
	GraphDas graphDas;
	private int gameSize = 8;			// Todo: Get form GameDas

	@FXML
	private Pane pane;
	@FXML
	private Button buttonTest;

	@FXML
	public void buttonClicked(ActionEvent event) {
		System.out.println("Started");
		buttonTest.setDisable(true);
		buttonTest.setText("Game is Started");
		// initCanvas(gameSize);
		loadGame();
	}

	private void loadGame() {
		gameFieldController = new GameFieldController(gameSize);
		pane.getChildren().add(gameFieldController);
		graphDas = GraphDasFactory.getGraphDas();
		gameFieldController.loadGame(graphDas.getRelevantFields());
	}

	@FXML
	public void addBridge(ActionEvent event) {
		System.out.println("Add Bridge");
		gameFieldController.addBridge(new GraphBridge(new GraphField(0, 1, 1), new GraphField(7, 1, 1)));
		gameFieldController.addBridge(new GraphBridge(new GraphField(6, 2, 1), new GraphField(6, 5, 2)));
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
