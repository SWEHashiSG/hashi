package ch.ntb.swehashisg.hashi.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.graph.GraphService;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Implementation of GameFieldController to play a game. This Controller is
 * normally used for the user.
 * 
 * @author Martin
 *
 */
public class GameFieldPlayController extends GameFieldController {

	private static final Logger logger = LoggerFactory.getLogger(GameFieldPlayController.class);

	/**
	 * Timer to start and stop the time how long the user has to play the game.
	 */
	private GameTime gameTime;

	/**
	 * Same constructor as super class only with the different, that the the
	 * gameTime is initialized.
	 * 
	 * @param graphService
	 *            Model of the MVC Pattern where the data is saved
	 * @param mainWindowController
	 *            Controller of the window where this game field will be placed
	 */
	public GameFieldPlayController(GraphService graphService, MainWindowController mainWindowController) {
		super(graphService, mainWindowController);
		gameTime = new GameTime();
	}

	/**
	 * Override the update method to check after the update, if the game has
	 * finished.
	 */
	@Override
	protected void update(GraphPlayField graphPlayField) {
		super.update(graphPlayField);
		if (graphService.isFinished(graphPlayField)) {
			finishGame();
		}
	}

	/**
	 * Show the user a dialog message for congratulation and show also how long
	 * he has used to play the game.
	 */
	private void finishGame() {
		gameTime.stopTime();
		logger.info("Game is Finished");
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Finish Game");
		alert.setHeaderText("Congratulation. You have finished the Hashi Game");
		alert.setContentText("Time: " + gameTime.getTime() + " Seconds");
		alert.showAndWait();
	}

	/**
	 * From template-pattern. returns the normal bridges
	 */
	@Override
	protected Set<GraphBridge> getBridges(GraphPlayField graphPlayField) {
		return graphPlayField.getBridges();
	}

	/**
	 * add a new bridge to the data model
	 */
	@Override
	protected void addBridge(GraphBridge graphBridge) {
		if (!gameTime.isRunning()) {
			gameTime.startTime();
		}
		graphService.addBridge(graphBridge);
	}

	/**
	 * remove a bridge from the data model
	 */
	@Override
	protected void removeBridge(GraphBridge graphBridge) {
		graphService.removeBridge(graphBridge);
	}
}
