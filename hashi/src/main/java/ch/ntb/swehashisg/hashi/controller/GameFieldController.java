package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;
import java.util.Set;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class GameFieldController extends GridPane{
	
	public GameFieldController(){
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GameField.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	
	public void loadGame(Set<GraphField> graphFields, Pane pane){
		for (GraphField graphField : graphFields) {
			add(new NodeController(graphField),graphField.getX(),graphField.getY());
		}
	}


	public void addBridge(GraphBridge graphBridge) {
		BridgeController bridge = new BridgeController(graphBridge);
		add(bridge,1,1);
	}

}
