package ch.ntb.swehashisg.hashi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.graph.GraphDas;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.input.MouseEvent;

/**
 * Implementation of GameFieldController to design a new game. This Controller
 * is only used in Editor-Mode that we can create our own games.
 * 
 * @author Martin
 *
 */
public class GameFieldDesignerController extends GameFieldController {

	private static final Logger logger = LoggerFactory.getLogger(GameFieldDesignerController.class);

	/**
	 * Same constructor as super class only with the different, that the grid
	 * lines are visible.
	 * 
	 * @param graphDas
	 *            Model of the MVC Pattern where the data is saved
	 * @param mainWindowController
	 *            Controller of the window where this game field will be placed
	 */
	public GameFieldDesignerController(GraphDas graphDas, MainWindowController mainWindowController) {
		super(graphDas, mainWindowController);
		setGridLinesVisible(true);
	}

	/**
	 * Shows a Dialog where the User can select the value of each field.
	 * 
	 * @return the selected value for the bridge. If nothing is selected, return
	 *         0
	 */
	private int showBridgeValueDialog() {
		List<Integer> choices = new ArrayList<>();
		choices.add(1);
		choices.add(2);
		choices.add(3);
		choices.add(4);
		choices.add(5);
		choices.add(6);
		choices.add(7);
		choices.add(8);
		ChoiceDialog<Integer> dialog = new ChoiceDialog<Integer>(1, choices);
		dialog.setTitle("New Field");
		dialog.setHeaderText("Choos Field Value");
		dialog.setContentText("value");

		Optional<Integer> result = dialog.showAndWait();
		if (result.isPresent()) {
			return result.get();
		} else {
			return 0;
		}
	}

	/**
	 * Override the clickedOnPane method where the it does nothing. get the
	 * position where the user has clicked and set a new bridge at this
	 * position.
	 */
	@Override
	protected void clickedOnPane(MouseEvent mouseEvent) {
		int x = ((int) mouseEvent.getX() / FieldController.getFieldSize());
		int y = ((int) mouseEvent.getY() / FieldController.getFieldSize());
		setBridges(x, y);
	}

	/**
	 * Override the clickedOnField method where it does nothing. Set another
	 * field at this position.
	 */
	@Override
	protected void clickedOnField(FieldController field) {
		setBridges(field.getGraphField().getX(), field.getGraphField().getY());
	}

	/**
	 * Show bridge value dialog for the user and add a new bridge at the
	 * position X and Y with the selected value.
	 * 
	 * @param x
	 *            position horizontal of the new field
	 * @param y
	 *            position vertical of the new field
	 */
	private void setBridges(int x, int y) {
		int value = showBridgeValueDialog();
		graphDas.setBridges(new GraphField(x, y, value));
		logger.debug("Clicked on Game Field in Designer Mode. Add new Field at: x=" + x + " - y=" + y);
		initiateUpdate();
	}

	/**
	 * From template-pattern. returns the solution Bridge
	 */
	@Override
	protected Set<GraphBridge> getBridges(GraphPlayField graphPlayField) {
		return graphPlayField.getSolutionBridges();
	}

	/**
	 * add a new solution bridge to the data model
	 */
	@Override
	protected void addBridge(GraphBridge graphBridge) {
		graphDas.addSolutionBridge(graphBridge);
	}

	/**
	 * remove a solution bridge from the data model
	 */
	@Override
	protected void removeBridge(GraphBridge graphBridge) {
		graphDas.removeSolutionBridge(graphBridge);
	}
}
