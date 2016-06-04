package ch.ntb.swehashisg.hashi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.model.GraphField;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;

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
	private Label label;
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
		// <Circle id="highliter" fx:id="highlight" fill="ORANGE"
		// mouseTransparent="true" radius="20.0" stroke="BLACK"
		// strokeType="INSIDE" strokeWidth="0.0" visible="false" />
		Circle circle1 = new Circle();
		circle1.setId("highliter");
		circle1.setFill(Paint.valueOf("ORANGE"));
		circle1.setMouseTransparent(true);
		circle1.setRadius(20);
		circle1.setStroke(Paint.valueOf("BLACK"));
		circle1.setStrokeType(StrokeType.INSIDE);
		circle1.setStrokeWidth(0);
		circle1.setVisible(false);
		highlight = circle1;
		this.getChildren().add(circle1);

		// <Circle centerX="20.0" centerY="20.0" fill="#ffffff00"
		// mouseTransparent="true" radius="20.0" stroke="BLACK"
		// strokeType="INSIDE" styleClass="field" />
		// .field{
		// -fx-stroke-width: 2.0;
		// }
		Circle circle2 = new Circle();
		circle2.setCenterX(20);
		circle2.setCenterY(20);
		circle2.setFill(Paint.valueOf("#ffffff00"));
		circle2.setMouseTransparent(true);
		circle2.setRadius(20);
		circle2.setStroke(Paint.valueOf("BLACK"));
		circle2.setStrokeType(StrokeType.INSIDE);
		circle2.setStrokeWidth(2);

		this.getChildren().add(circle2);

		// <Label fx:id="label" mouseTransparent="true" text="0">
		// <font>
		// <Font size="18.0" />
		// </font>
		// </Label>
		label = new Label();
		label.setId("label");
		label.setMouseTransparent(true);
		label.setFont(Font.font(18));

		this.getChildren().add(label);
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				onMouseClicked();
			}
		});

		this.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				onMouseEntered();
			}
		});

		this.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				onMouseExited(event);
			}
		});

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
	protected void onMouseClicked() {
		logger.debug("Clicked on Field! X=" + graphField.getX() + "  Y=" + graphField.getY());
		gameField.clickedOnField(this);
	}

	/**
	 * User input if he move with the mouse on this field, the highlight appear
	 * and also the highlight from all possible bridges will appear.
	 */
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
