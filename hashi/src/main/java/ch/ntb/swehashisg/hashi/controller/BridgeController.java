package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.model.BridgeDirection;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
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

	private static final Logger logger = LoggerFactory.getLogger(BridgeController.class);

	/**
	 * JavaFX Attributes from the FXML-File. The bridges are created as three
	 * different Panes and are change the visibility to draw the bridges.
	 * lineMiddle to draw one bridge in the middle of two Fields. lineTop and
	 * lineButtom to draw to bridges
	 */
	@FXML
	private Pane lineTop;
	@FXML
	private Pane lineMiddle;
	@FXML
	private Pane lineButtom;

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
		String fxmlFile = "";
		if (graphBridge.getBridgeDirection() == BridgeDirection.Horizontal) {
			fxmlFile = "/fxml/HorizontalBridge.fxml";
		} else {
			fxmlFile = "/fxml/VerticalBridge.fxml";
		}
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		this.graphBridge = graphBridge;
		this.gameFieldController = gameFieldController;

		if (graphBridge.getWeighting() > 0) {
			if (graphBridge.getWeighting() == 1) {
				lineMiddle.setVisible(true);
			} else if (graphBridge.getWeighting() == 2) {
				lineButtom.setVisible(true);
				lineTop.setVisible(true);
			}
		}
		this.setVisible(isVisible);
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
