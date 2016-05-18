package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;

import ch.ntb.swehashisg.hashi.model.BridgeDirection;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class Bridge extends StackPane {

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

	public Bridge(GraphBridge graphBridge, GameField gameField, int weighting) {
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
		setWeighting(weighting);
		highliter.setVisible(false);
	}
	
	public void setWeighting(int weighting){
		this.weighting = weighting;
		redrawBridge();
	}

	private void redrawBridge() {
		switch (weighting) {
		case 0:
			lineMiddle.setVisible(false);
			lineTop.setVisible(false);
			lineButtom.setVisible(false);
			break;
		case 1:
			lineMiddle.setVisible(true);
			lineTop.setVisible(false);
			lineButtom.setVisible(false);
			break;
		case 2:
			lineMiddle.setVisible(false);
			lineTop.setVisible(true);
			lineButtom.setVisible(true);
			break;
		default:
			throw new IllegalArgumentException(
					"Weighting of Bridge is between 0 and 2. Weighting " + weighting + " is out of Range");
		}
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

	public void highlite() {
		highliter.setVisible(true);
		// gameField.addBridge(graphBridge);
	}

	@FXML
	protected void onMouseClicked() {
		System.out.println("Clicked on Bridge!");
		gameField.addBridge(graphBridge);
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
