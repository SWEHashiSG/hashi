package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.model.GraphField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

/**
 * Controller class for the JavaFX view Field. Set the Number of needed Bridges
 * on the view and disable the number if there are enough bridges connected.
 * 
 * @author Martin
 *
 */
public class FieldController extends StackPane {

	private static Logger logger = LoggerFactory.getLogger(FieldController.class);

	/**
	 * Size of the field that is needed to calculate the Position and Size of
	 * the game field.
	 */
	private static int fieldSize = 40;

	/**
	 * JavaFX Attributes from the FXML-File. The field has a label for the
	 * number and also a highlight which toggle its visibility when the mouse is
	 * on the field.
	 */
	@FXML
	private Label label;
	@FXML
	private Circle highlight;

	/**
	 * the field which associate to this controller
	 */
	private GraphField graphField;

	/**
	 * controller of the Game field where the field will be drawn.
	 */
	private GameFieldController gameField;

	/**
	 * Constructor of the FieldController. Loads the FXML-file and sets the
	 * number of needed bridges in the label. Also disable the label if enough
	 * bridges are connected.
	 * 
	 * @param graphField
	 *            the field which associate to this controller
	 * @param gameField
	 *            controller of the Game field where the field will be drawn.
	 */
	public FieldController(GraphField graphField, GameFieldController gameField) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Field.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		this.graphField = graphField;
		this.gameField = gameField;
		label.setText(Integer.toString(graphField.getBridges()));
		if (graphField.getExistingBridges().size() == graphField.getBridges()) {
			setDisable(true);
		}
	}

	/**
	 * add the field to the gameField simply decided on the X and Y position
	 */
	public void addToGameField() {
		gameField.add(this, graphField.getX(), graphField.getY());
	}

	/**
	 * User input if he clicks on the field
	 */
	@FXML
	protected void onMouseClicked() {
		logger.debug("Clicked on Field! X=" + graphField.getX() + "  Y=" + graphField.getY());
		gameField.clickedOnField(this);
	}

	/**
	 * User input if he move with the mouse on this field, the highlight appear
	 * and also the highlight from all possible bridges will appear.
	 */
	@FXML
	protected void onMouseEntered() {
		logger.debug("Mouse on Field! X=" + graphField.getX() + "  Y=" + graphField.getY());
		highlight.setVisible(true);
		setHighlighterFromBridges(true);

	}

	/**
	 * User input if he move with the mouse from this field away. The highlight
	 * will disappear and also all highlight of the possible bridges with
	 * exception of the possible bridge where the mouse has moved on.
	 * 
	 * @param event
	 *            MouseEvent to get the actual position of the mouse
	 */
	@FXML
	protected void onMouseExited(MouseEvent event) {
		highlight.setVisible(false);
		setHighlighterFromBridges(false);
		if (isMouseWest(event) && graphField.hasWestNeighbor()) {
			logger.debug(" mouse leave Field to west");
			HighlightController bridge = gameField.getWestHighlight(this);
			if (gameField.needsBridge(bridge)) {
				bridge.setHighlite(true);
			}
		} else if (isMouseEast(event) && graphField.hasEastNeighbor()) {
			logger.debug(" mouse leave Field to east");
			HighlightController bridge = gameField.getEastHighlight(this);
			if (gameField.needsBridge(bridge)) {
				bridge.setHighlite(true);
			}
		} else if (isMouseNorth(event) && graphField.hasNorthNeighbor()) {
			logger.debug(" mouse leave Field to north");
			HighlightController bridge = gameField.getNorthHighlight(this);
			if (gameField.needsBridge(bridge)) {
				bridge.setHighlite(true);
			}
		} else if (isMouseSouth(event) && graphField.hasSouthNeighbor()) {
			logger.debug(" mouse leave Field to south");
			HighlightController bridge = gameField.getSouthHighlight(this);
			if (gameField.needsBridge(bridge)) {
				bridge.setHighlite(true);
			}
		}
	}

	/**
	 * @param event
	 *            MouseEvent form the actual position of the mouse
	 * @return true if mouse is moved to the south bridge
	 */
	private boolean isMouseSouth(MouseEvent event) {
		return event.getY() >= fieldSize && event.getX() > fieldSize / 3 && event.getX() < fieldSize * 2 / 3;
	}

	/**
	 * @param event
	 *            MouseEvent form the actual position of the mouse
	 * @return true if mouse is moved to the north bridge
	 */
	private boolean isMouseNorth(MouseEvent event) {
		return event.getY() <= 0 && event.getX() > fieldSize / 3 && event.getX() < fieldSize * 2 / 3;
	}

	/**
	 * @param event
	 *            MouseEvent form the actual position of the mouse
	 * @return true if mouse is moved to the east bridge
	 */
	private boolean isMouseEast(MouseEvent event) {
		return event.getX() >= fieldSize && event.getY() > fieldSize / 3 && event.getY() < fieldSize * 2 / 3;
	}

	/**
	 * @param event
	 *            MouseEvent form the actual position of the mouse
	 * @return true if mouse is moved to the west bridge
	 */
	private boolean isMouseWest(MouseEvent event) {
		return event.getX() <= 0 && event.getY() > fieldSize / 3 && event.getY() < fieldSize * 2 / 3;
	}

	/**
	 * Set the highlight on or of if there is a possible bridge and checks also
	 * if there is a bridge needed.
	 * 
	 * @param highlited
	 *            true of false if should be visible or not.
	 */
	private void setHighlighterFromBridges(boolean highlight) {
		if (graphField.hasWestNeighbor()) {
			HighlightController bridge = gameField.getWestHighlight(this);
			if (gameField.needsBridge(bridge)) {
				bridge.setHighlite(highlight);
			}
		}
		if (graphField.hasEastNeighbor()) {
			HighlightController bridge = gameField.getEastHighlight(this);
			if (gameField.needsBridge(bridge)) {
				bridge.setHighlite(highlight);
			}
		}
		if (graphField.hasNorthNeighbor()) {
			HighlightController bridge = gameField.getNorthHighlight(this);
			if (gameField.needsBridge(bridge)) {
				bridge.setHighlite(highlight);
			}
		}
		if (graphField.hasSouthNeighbor()) {
			HighlightController bridge = gameField.getSouthHighlight(this);
			if (gameField.needsBridge(bridge)) {
				bridge.setHighlite(highlight);
			}
		}
	}

	public GraphField getGraphField() {
		return graphField;
	}

	public void setGraphField(GraphField graphField) {
		this.graphField = graphField;
	}

	public static int getFieldSize() {
		return fieldSize;
	}
}