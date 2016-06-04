package ch.ntb.swehashisg.hashi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.model.BridgeDirection;
import ch.ntb.swehashisg.hashi.model.GraphField;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Controller class for the JavaFX view HorizontalHighlight and
 * VerticalHighlight. The JavaFX file is split into horizontal and vertical
 * Highlight. The constructor of this class decides which JavaFX element would
 * be draw due to the given GraphBridge. The highlight will be created for all
 * possible bridges but is only visible if the mouse is on the Bridge or on a
 * connected Field.
 * 
 * @author Martin
 *
 */
public class HighlightController extends StackPane {

	private static final Logger logger = LoggerFactory.getLogger(HighlightController.class);

	/**
	 * JavaFX Attributes from the FXML-File. Change the visibility of the
	 * highlight by changing its visibility.
	 */
	private Pane highlight;

	/**
	 * The two neighbors were the possible bridge associate to.
	 */
	private GraphField neighbor1;
	private GraphField neighbor2;

	/**
	 * Controller of the Game field where the Highlight will be drawn.
	 */
	private GameFieldController gameField;

	/**
	 * Main constructor of the BridgeController. Decides which highlight would
	 * be loaded (horizontal or vertical). Set default visibility to false
	 * 
	 * @param neighbor1
	 *            one end of the possible bridge
	 * @param neighbor2
	 *            the other end of the possible bridge
	 * @param gameField
	 *            controller of the Game field where the Highlight will be
	 *            drawn.
	 */
	public HighlightController(GraphField neighbor1, GraphField neighbor2, GameFieldController gameField) {
		// <fx:root minHeight="40" minWidth="40"
		// type="javafx.scene.layout.StackPane"
		// xmlns="http://javafx.com/javafx/8"
		// xmlns:fx="http://javafx.com/fxml/1" onMouseEntered="#onMouseEntered"
		// onMouseExited="#onMouseExited" onMouseClicked="#onMouseClicked">
		this.minHeight(40);
		this.minWidth(40);
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				onMouseClicked(event);
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
				onMouseExited();
			}
		});
		GridPane gridPane = null;
		if (GraphUtil.getDirectionOfNeighbors(neighbor1, neighbor2) == BridgeDirection.Horizontal) {
			gridPane = horizontalGridpane();
		} else {
			gridPane = verticalGridpane();
		}

		this.getChildren().add(gridPane);

		this.neighbor1 = neighbor1;
		this.neighbor2 = neighbor2;
		this.gameField = gameField;
		setHighlite(false);
	}

	private GridPane horizontalGridpane() {
		// <GridPane>
		// <columnConstraints>
		// <ColumnConstraints hgrow="ALWAYS" />
		// </columnConstraints>
		// <rowConstraints>
		// <RowConstraints percentHeight="33.3" />
		// <RowConstraints percentHeight="33.3" />
		// <RowConstraints />
		// </rowConstraints>
		// <children>
		// <Pane fx:id="highlight" style="-fx-background-color: Orange;"
		// visible="false" GridPane.rowIndex="1" />
		// </children>
		// </GridPane>
		GridPane gridPane = new GridPane();

		ColumnConstraints column = new ColumnConstraints();
		column.setHgrow(Priority.ALWAYS);
		gridPane.getColumnConstraints().add(column);
		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(33.3);
		gridPane.getRowConstraints().add(row1);
		RowConstraints row2 = new RowConstraints();
		row2.setPercentHeight(33.3);
		gridPane.getRowConstraints().add(row2);
		RowConstraints row3 = new RowConstraints();
		gridPane.getRowConstraints().add(row3);

		highlight = new Pane();
		highlight.setStyle("-fx-background-color: Orange;");
		highlight.setVisible(false);

		gridPane.add(highlight, 0, 1);
		return gridPane;
	}

	private GridPane verticalGridpane() {
		// <GridPane>
		// <columnConstraints>
		// <ColumnConstraints percentWidth="33.3" />
		// <ColumnConstraints percentWidth="33.3" />
		// <ColumnConstraints />
		// </columnConstraints>
		// <rowConstraints>
		// <RowConstraints vgrow="ALWAYS" />
		// </rowConstraints>
		// <children>
		// <Pane fx:id="highlight" style="-fx-background-color: Orange;"
		// visible="false" GridPane.columnIndex="1" />
		// </children>
		// </GridPane>
		GridPane gridPane = new GridPane();

		RowConstraints column = new RowConstraints();
		column.setVgrow(Priority.ALWAYS);
		gridPane.getRowConstraints().add(column);
		ColumnConstraints row1 = new ColumnConstraints();
		row1.setPercentWidth(33.3);
		gridPane.getColumnConstraints().add(row1);
		ColumnConstraints row2 = new ColumnConstraints();
		row2.setPercentWidth(33.3);
		gridPane.getColumnConstraints().add(row2);

		highlight = new Pane();
		highlight.setStyle("-fx-background-color: Orange;");
		highlight.setVisible(false);

		gridPane.add(highlight, 1, 0);
		return gridPane;
	}

	/**
	 * Add the highlight to the GameField by taking the position from the
	 * GraphBridge and calculate the span for different lengths of the bridges.
	 */
	public void addToGameField() {
		int columnIndex = neighbor1.getX();
		int rowIndex = neighbor1.getY();
		int columnSpan = 1;
		int rowSpan = 1;
		if (GraphUtil.getDirectionOfNeighbors(neighbor1, neighbor2) == BridgeDirection.Vertical) {
			if (neighbor1.getY() > neighbor2.getY()) {
				rowIndex = neighbor2.getY();
			}
			rowIndex++;
			rowSpan = Math.abs(neighbor1.getY() - neighbor2.getY()) - 1;
		} else {
			if (neighbor1.getX() > neighbor2.getX()) {
				columnIndex = neighbor2.getX();
			}
			columnIndex++;
			columnSpan = Math.abs(neighbor1.getX() - neighbor2.getX()) - 1;
		}
		gameField.add(this, columnIndex, rowIndex, columnSpan, rowSpan);
	}

	/**
	 * set the visibility of the highlight. By default the highlight is mouse
	 * transparent. If there is one ore more bridges in front the highlight is
	 * listening on mouse events.
	 * 
	 * @param visible
	 */
	public void setHighlite(boolean visible) {
		highlight.setVisible(visible);
		if (gameField.getBridge(neighbor1, neighbor2).getWeighting() > 0) {
			highlight.setMouseTransparent(false);
			this.setMouseTransparent(false);
		} else {
			highlight.setMouseTransparent(!visible);
			this.setMouseTransparent(!visible);
		}
	}

	public GraphField getNeighbor1() {
		return neighbor1;
	}

	public GraphField getNeighbor2() {
		return neighbor2;
	}

	/**
	 * User input if he clicks on the highlight. Add a bridge if both fields
	 * needs one. Otherwise remove all bridges.
	 * 
	 * @param event
	 */
	@FXML
	protected void onMouseClicked(MouseEvent event) {
		event.consume(); // Consume Event that nothing in Background will be
							// activated
		logger.debug("Clicked on Highliter");
		if (gameField.needsBridge(this)) {
			gameField.addBridge(this);
		} else {
			gameField.removeBridge(this);
		}
	}

	/**
	 * User input if he move with the mouse on this highlight. if there is
	 * already one ore more bridges the highlight gets visible. Otherwise not
	 */
	@FXML
	protected void onMouseEntered() {
		logger.debug("Mouse on Highliter");
		if (gameField.hasBridge(neighbor1, neighbor2)) {
			setHighlite(true);
		}
	}

	/**
	 * User input if he move with the mouse from this highlight the highlight
	 * will disappear.
	 */
	@FXML
	protected void onMouseExited() {
		logger.debug("Mouse not on Highliter");
		setHighlite(false);
	}

	/**
	 * mark the highlight Read to signal the user a fault.
	 */
	public void markRed() {
		setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10), Insets.EMPTY)));
	}
}
