package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.model.BridgeDirection;
import ch.ntb.swehashisg.hashi.model.GraphField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class Highlight extends StackPane {

	private static final Logger log = LoggerFactory.getLogger(Highlight.class);

	@FXML
	private Pane lineTop;
	@FXML
	private Pane lineMiddle;
	@FXML
	private Pane lineButtom;
	@FXML
	private Pane highliter;

	private GraphField neighbor1;
	private GraphField neighbor2;
	private GameField gameField;

	public Highlight(GraphField neighbor1, GraphField neighbor2, GameField gameField) {
		String fxmlFile = "";
		if (GraphUtil.getDirectionOfNeighbors(neighbor1, neighbor2) == BridgeDirection.Horizontal) {
			fxmlFile = "/fxml/HorizontalHighlight.fxml";
		} else {
			fxmlFile = "/fxml/VerticalHighlight.fxml";
		}
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		this.neighbor1 = neighbor1;
		this.neighbor2 = neighbor2;
		this.gameField = gameField;
	}

	public void addToGameField() {
		int columnIndex = neighbor1.getX();
		int rowIndex = neighbor2.getY();
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

	public void setHighlite(boolean highlited) {
		log.debug("Should highlight be visible: " + highlited);
		highliter.setVisible(highlited);
	}

	@FXML
	protected void onMouseEntered() {
		if (gameField.hasBridge(neighbor1, neighbor2)) {
			setHighlite(true);
		}
	}

	@FXML
	protected void onMouseExited() {
		log.debug("Mouse not on Bridge:-)");
		setHighlite(false);
	}
}
