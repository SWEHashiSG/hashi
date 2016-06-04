package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.model.BridgeDirection;
import ch.ntb.swehashisg.hashi.model.GraphField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class HighlightController extends StackPane {

	private static final Logger logger = LoggerFactory.getLogger(HighlightController.class);

	@FXML
	private Pane highliter;

	private GraphField neighbor1;
	private GraphField neighbor2;
	private GameFieldController gameField;

	public HighlightController(GraphField neighbor1, GraphField neighbor2, GameFieldController gameField) {
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
		setHighlite(false);
	}

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

	public void setHighlite(boolean highlited) {
		highliter.setVisible(highlited);
		if (gameField.getBridge(neighbor1, neighbor2).getWeighting() > 0){
			highliter.setMouseTransparent(false);
			this.setMouseTransparent(false);	
		} else {
			highliter.setMouseTransparent(!highlited);
			this.setMouseTransparent(!highlited);
		}
	}

	public GraphField getNeighbor1() {
		return neighbor1;
	}

	public GraphField getNeighbor2() {
		return neighbor2;
	}

	@FXML
	protected void onMouseClicked(MouseEvent event) {
		event.consume();					// Consume Event that not a new Field will be drawn
		logger.debug("Clicked on Highliter");
		if(gameField.needsBridge(this)) {
			gameField.addBridge(this);	
		} else {
			gameField.removeBridge(this);
		}
	}

	@FXML
	protected void onMouseEntered() {
		logger.debug("Mouse on Highliter");
		if (gameField.hasBridge(neighbor1, neighbor2)) {
			setHighlite(true);
		}
	}

	@FXML
	protected void onMouseExited() {
		logger.debug("Mouse not on Highliter");
		setHighlite(false);
	}

	public void markRed() {
		setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10), Insets.EMPTY)));
	}
}
