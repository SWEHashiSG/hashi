package ch.ntb.swehashisg.hashi.controller;

import ch.ntb.swehashisg.hashi.model.BridgeDirection;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

/**
 * Controller class for the JavaFX view HorizontalBridge and VerticalBridge. The
 * JavaFX file is split into horizontal and vertical Bridge. The constructor of
 * this class decides which JavaFX element would be draw due to the given
 * GraphBridge.
 * 
 * @author Martin
 *
 */
public class BridgeController extends StackPane {

	/**
	 * JavaFX Attributes from the FXML-File. The bridges are created as three
	 * different Panes and are change the visibility to draw the bridges.
	 * lineMiddle to draw one bridge in the middle of two Fields. lineTop and
	 * lineButtom to draw to bridges
	 */
	private Pane lineTop;
	private Pane lineMiddle;
	private Pane lineBottom;

	/**
	 * the bridge which associate to this controller
	 */
	private GraphBridge graphBridge;

	/**
	 * controller of the Game field where the bridge will be drawn.
	 */
	private GameFieldController gameFieldController;

	/**
	 * Simple constructor of the BridgeController without isVisible
	 * 
	 * @param graphBridge
	 *            the bridge which associate to this controller
	 * @param gameFieldController
	 *            controller of the Game field where the bridge will be drawn.
	 */
	public BridgeController(GraphBridge graphBridge, GameFieldController gameFieldController) {
		this(graphBridge, gameFieldController, true);
	}

	/**
	 * Main constructor of the BridgeController. Decides which bridge would be
	 * loaded (horizontal or vertical). Set bridges from the weighting of the
	 * GraphBridge and also sets the default visibility.
	 * 
	 * @param graphBridge
	 *            the bridge which associate to this controller
	 * @param gameFieldController
	 *            controller of the Game field where the bridge will be drawn.
	 * @param isVisible
	 *            Sets the default Visibility of the Bridge
	 */
	public BridgeController(GraphBridge graphBridge, GameFieldController gameFieldController, boolean isVisible) {
		// fx:root mouseTransparent="true" minHeight="40" minWidth="40"
		// type="javafx.scene.layout.StackPane"
		// xmlns="http://javafx.com/javafx/8"
		// xmlns:fx="http://javafx.com/fxml/1">
		this.setMouseTransparent(true);
		this.setMinHeight(40);
		this.setMinWidth(40);

		GridPane gridPane = null;

		if (graphBridge.getBridgeDirection() == BridgeDirection.Horizontal) {
			gridPane = horizontalGridpane();
			;
		} else {
			gridPane = verticalGridpane();
		}

		this.getChildren().add(gridPane);

		this.graphBridge = graphBridge;
		this.gameFieldController = gameFieldController;

		if (graphBridge.getWeighting() > 0) {
			if (graphBridge.getWeighting() == 1) {
				lineMiddle.setVisible(true);
			} else if (graphBridge.getWeighting() == 2) {
				lineBottom.setVisible(true);
				lineTop.setVisible(true);
			}
		}
		this.setVisible(isVisible);
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
		// <GridPane GridPane.rowIndex="1">
		// <columnConstraints>
		// <ColumnConstraints hgrow="ALWAYS" />
		// </columnConstraints>
		// <rowConstraints>
		// <RowConstraints percentHeight="20.0" vgrow="ALWAYS" />
		// <RowConstraints percentHeight="20.0" vgrow="ALWAYS" />
		// <RowConstraints percentHeight="20.0" vgrow="ALWAYS" />
		// <RowConstraints percentHeight="20.0" vgrow="ALWAYS" />
		// <RowConstraints percentHeight="20.0" vgrow="ALWAYS" />
		// </rowConstraints>
		// <children>
		// <Pane fx:id="lineTop" style="-fx-background-color: black;"
		// visible="false" GridPane.rowIndex="1" />
		// <Pane fx:id="lineMiddle" style="-fx-background-color: black;"
		// visible="false" GridPane.rowIndex="2" />
		// <Pane fx:id="lineButtom" style="-fx-background-color: black;"
		// visible="false" GridPane.rowIndex="3" />
		// </children>
		// </GridPane>
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

		GridPane gridPane2 = new GridPane();
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setHgrow(Priority.ALWAYS);
		gridPane2.getColumnConstraints().add(column2);
		RowConstraints row4 = new RowConstraints();
		row4.setPercentHeight(20);
		row4.setVgrow(Priority.ALWAYS);
		gridPane2.getRowConstraints().add(row4);
		RowConstraints row5 = new RowConstraints();
		row5.setPercentHeight(20);
		row5.setVgrow(Priority.ALWAYS);
		gridPane2.getRowConstraints().add(row5);
		RowConstraints row6 = new RowConstraints();
		row6.setPercentHeight(20);
		row6.setVgrow(Priority.ALWAYS);
		gridPane2.getRowConstraints().add(row6);
		RowConstraints row7 = new RowConstraints();
		row7.setPercentHeight(20);
		row7.setVgrow(Priority.ALWAYS);
		gridPane2.getRowConstraints().add(row7);
		RowConstraints row8 = new RowConstraints();
		row8.setPercentHeight(20);
		row8.setVgrow(Priority.ALWAYS);
		gridPane2.getRowConstraints().add(row8);

		lineTop = new Pane();
		lineTop.setStyle("-fx-background-color: black;");
		lineTop.setVisible(false);
		gridPane2.add(lineTop, 0, 1);
		lineMiddle = new Pane();
		lineMiddle.setStyle("-fx-background-color: black;");
		lineMiddle.setVisible(false);
		gridPane2.add(lineMiddle, 0, 2);
		lineBottom = new Pane();
		lineBottom.setStyle("-fx-background-color: black;");
		lineBottom.setVisible(false);
		gridPane2.add(lineBottom, 0, 3);

		gridPane.add(gridPane2, 0, 1);
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
		// <GridPane GridPane.columnIndex="1">
		// <columnConstraints>
		// <ColumnConstraints percentWidth="20.0" />
		// <ColumnConstraints percentWidth="20.0" />
		// <ColumnConstraints percentWidth="20.0" />
		// <ColumnConstraints percentWidth="20.0" />
		// </columnConstraints>
		// <rowConstraints>
		// <RowConstraints vgrow="ALWAYS" />
		// </rowConstraints>
		// <children>
		// <Pane fx:id="lineTop" style="-fx-background-color: black;"
		// visible="false" GridPane.columnIndex="1" />
		// <Pane fx:id="lineMiddle" style="-fx-background-color: black;"
		// visible="false" GridPane.columnIndex="2" />
		// <Pane fx:id="lineButtom" style="-fx-background-color: black;"
		// visible="false" GridPane.columnIndex="3" />
		// </children>
		// </GridPane>
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

		GridPane gridPane2 = new GridPane();
		RowConstraints column2 = new RowConstraints();
		column2.setVgrow(Priority.ALWAYS);
		gridPane2.getRowConstraints().add(column2);
		ColumnConstraints row4 = new ColumnConstraints();
		row4.setPercentWidth(20);
		row4.setHgrow(Priority.ALWAYS);
		gridPane2.getColumnConstraints().add(row4);
		ColumnConstraints row5 = new ColumnConstraints();
		row5.setPercentWidth(20);
		row5.setHgrow(Priority.ALWAYS);
		gridPane2.getColumnConstraints().add(row5);
		ColumnConstraints row6 = new ColumnConstraints();
		row6.setPercentWidth(20);
		row6.setHgrow(Priority.ALWAYS);
		gridPane2.getColumnConstraints().add(row6);
		ColumnConstraints row7 = new ColumnConstraints();
		row7.setPercentWidth(20);
		row7.setHgrow(Priority.ALWAYS);
		gridPane2.getColumnConstraints().add(row7);

		lineTop = new Pane();
		lineTop.setStyle("-fx-background-color: black;");
		lineTop.setVisible(false);
		gridPane2.add(lineTop, 1, 0);
		lineMiddle = new Pane();
		lineMiddle.setStyle("-fx-background-color: black;");
		lineMiddle.setVisible(false);
		gridPane2.add(lineMiddle, 2, 0);
		lineBottom = new Pane();
		lineBottom.setStyle("-fx-background-color: black;");
		lineBottom.setVisible(false);
		gridPane2.add(lineBottom, 3, 0);

		gridPane.add(gridPane2, 1, 0);
		return gridPane;
	}

	/**
	 * add the BridgeController to the GameField by taking the position from the
	 * GraphBridge and calculate the span for different lengths of the bridges
	 */
	public void addToGameField() {
		int columnIndex = graphBridge.getField1().getX();
		int rowIndex = graphBridge.getField1().getY();
		int columnSpan = 1;
		int rowSpan = 1;
		if (graphBridge.getBridgeDirection() == BridgeDirection.Vertical) {
			if (graphBridge.getField1().getY() > graphBridge.getField2().getY()) {
				rowIndex = graphBridge.getField2().getY();
			}
			rowIndex++;
			rowSpan = Math.abs(graphBridge.getField1().getY() - graphBridge.getField2().getY()) - 1;
		} else {
			if (graphBridge.getField1().getX() > graphBridge.getField2().getX()) {
				columnIndex = graphBridge.getField2().getX();
			}
			columnIndex++;
			columnSpan = Math.abs(graphBridge.getField1().getX() - graphBridge.getField2().getX()) - 1;
		}
		gameFieldController.add(this, columnIndex, rowIndex, columnSpan, rowSpan);
	}

	public GraphBridge getGraphBridge() {
		return graphBridge;
	}

	public void toggleVisibility() {
		if (this.isVisible()) {
			this.setVisible(false);
		} else {
			this.setVisible(true);
		}
	}
}
