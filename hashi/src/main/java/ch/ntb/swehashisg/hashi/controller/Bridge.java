package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.MainApp;
import ch.ntb.swehashisg.hashi.model.BridgeDirection;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class Bridge extends StackPane {

	private static final Logger log = LoggerFactory.getLogger(Bridge.class);

	@FXML
	private Pane lineTop;
	@FXML
	private Pane lineMiddle;
	@FXML
	private Pane lineButtom;
	@FXML
	private Pane highliter;

	private GraphBridge graphBridge;
	private GameField gameField;
	private int weighting;

	public Bridge(GraphBridge graphBridge, GameField gameField) {
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
		this.gameField = gameField;
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
		gameField.add(this, columnIndex, rowIndex, columnSpan, rowSpan);
	}

	public void setHighlite(boolean highlited) {
		highliter.setVisible(highlited);
	}

	public GraphBridge getGraphBridge() {
		return graphBridge;
	}

	@FXML
	protected void onMouseClicked() {
		log.debug("Clicked on Bridge!");
		// TODO: Add Bridge
		// gameField.addBridge(graphBridge);
	}

	@FXML
	protected void onMouseEntered() {
		if (graphBridge.getWeighting() > 0) {
			log.debug("Mouse on Bridge:-)");
			graphBridge.setHighliter(true);
		}
	}

	@FXML
	protected void onMouseExited() {
		log.debug("Mouse not on Bridge:-)");
		setHighlite(false);
	}
}
