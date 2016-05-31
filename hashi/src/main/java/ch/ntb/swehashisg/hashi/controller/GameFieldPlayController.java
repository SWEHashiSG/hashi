package ch.ntb.swehashisg.hashi.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.graph.GraphDas;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class GameFieldPlayController extends GameFieldController {

	private static final Logger logger = LoggerFactory.getLogger(GameFieldPlayController.class);

	private GameTime gameTime;

	public GameFieldPlayController(GraphDas graphDas) {
		super(graphDas);
		gameTime = new GameTime();
	}

	@Override
	protected void update(GraphPlayField graphPlayField) {
		super.update(graphPlayField);
		if (graphDas.isCorrect()) {
			finishGame();
		}
	}

	private void finishGame() {
		gameTime.stopTime();
		logger.info("Game is Finished");
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Finish GAme");
		alert.setHeaderText("Congratulation. You have finished the Hashi Game");
		alert.setContentText("Time: " + gameTime.getTime() + " Seconds");
		alert.showAndWait();
	}

	@Override
	void onMouseClicked(MouseEvent event) {
		logger.debug("Clicked on Game Field. No Function in Play Mode");
		// Nothing to do in Play Mode
	}

	@Override
	protected Set<GraphBridge> getBridges(GraphPlayField graphPlayField) {
		return graphPlayField.getBridges();
	}

	@Override
	protected void addBridge(GraphBridge graphBridge) {
		if (!gameTime.isRunning()) {
			gameTime.startTime();
		}
		graphDas.addBridge(graphBridge);
	}

	@Override
	protected void removeBridge(GraphBridge graphBridge) {
		graphDas.removeBridge(graphBridge);
	}
}
