package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;

import ch.ntb.swehashisg.hashi.model.BridgeDirection;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class BridgeController extends StackPane {

	@FXML
	private Pane lineTop;
	@FXML
	private Pane lineMiddle;
	@FXML
	private Pane lineButtom;
	@FXML
	private Pane highliter;

	private GraphBridge graphBridge;

	public BridgeController(GraphBridge graphBridge) {
		String fxmlFile = "";
		if (graphBridge.getBridgeDirection() == BridgeDirection.Horizontal) {
			fxmlFile = "/fxml/Bridge.fxml";
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
		if (graphBridge.getBridgeDirection() == BridgeDirection.Vertical) {
			// this turn
		}
		lineMiddle.setVisible(false);
		lineTop.setVisible(true);
		lineButtom.setVisible(true);
		highliter.setVisible(false);
	}

	public void addToGameField(GridPane gridPane) {
		int columnIndex = graphBridge.getField1().getX();
		int rowIndex = graphBridge.getField1().getY();
		int columnSpan = 1;
		int rowSpan = 1;
		if (graphBridge.getBridgeDirection() == BridgeDirection.Vertical) {
			rowIndex++;
			rowSpan = Math.abs(graphBridge.getField1().getY() - graphBridge.getField2().getY()) - 1;
		} else {
			columnIndex++;
			columnSpan = Math.abs(graphBridge.getField1().getX() - graphBridge.getField2().getX()) - 1;
		}
		gridPane.add(this, columnIndex, rowIndex, columnSpan, rowSpan);
	}

	@FXML
	protected void onMouseClicked() {
		System.out.println("Clicked on Bridge!");
	}

	@FXML
	protected void onMouseEntered() {
		System.out.println("Mouse on Bridge:-)");
		highliter.setVisible(true);
	}

	@FXML
	protected void onMouseExited() {
		System.out.println("Mouse not on Bridge:-)");
		highliter.setVisible(false);
	}
}
