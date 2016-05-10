package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class BridgeController extends VBox {

	@FXML
	private Rectangle highliterVertical;
	@FXML
	private Rectangle highliterHorizontal;
	@FXML
	private Line lineVerticalSingle;
	@FXML
	private Line lineHorizontalSingle;
	@FXML
	private Line lineHorizontalDouble1;
	@FXML
	private Line lineHorizontalDouble2;
	@FXML
	private Line lineVerticalDouble1;
	@FXML
	private Line lineVerticalDouble2;
	
	private GraphBridge graphBridge;

	public BridgeController(GraphBridge graphBridge) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Bridge.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		this.graphBridge = graphBridge;
	}

	@FXML
	protected void onMouseClicked() {
		System.out.println("The button was clicked!");
	}

	@FXML
	protected void onMouseEntered() {
		System.out.println("Mouse on Node:-)");
	}

	@FXML
	protected void onMouseExited() {
		System.out.println("Mouse not on Node:-)");
	}
}
