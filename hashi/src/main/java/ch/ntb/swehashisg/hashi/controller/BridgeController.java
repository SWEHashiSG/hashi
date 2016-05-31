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

public class BridgeController extends StackPane {

	private static final Logger logger = LoggerFactory.getLogger(BridgeController.class);

	@FXML
	private Pane lineTop;
	@FXML
	private Pane lineMiddle;
	@FXML
	private Pane lineButtom;

	private GraphBridge graphBridge;
	private GameFieldController gameFieldController;

	public BridgeController(GraphBridge graphBridge, GameFieldController gameFieldController) {
		this(graphBridge, gameFieldController, true);
	}

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
