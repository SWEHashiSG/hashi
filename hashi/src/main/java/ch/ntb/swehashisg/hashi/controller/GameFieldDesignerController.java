package ch.ntb.swehashisg.hashi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.graph.GraphDas;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.input.MouseEvent;

public class GameFieldDesignerController extends GameFieldController {

	private static final Logger logger = LoggerFactory.getLogger(GameFieldDesignerController.class);

	public GameFieldDesignerController(GraphDas graphDas) {
		super(graphDas);

		setGridLinesVisible(true); // TODO: Does not Working
	}

	protected void update(GraphPlayField graphPlayField) {
		graphFields = graphPlayField.getFields();
		loadGame();
		isUpdating = false;
	}

	@FXML
	void onMouseClicked(MouseEvent mouseEvent) {
		int x = ((int) mouseEvent.getX() / FieldController.getFieldSize());
		int y = ((int) mouseEvent.getY() / FieldController.getFieldSize());
		addNewField(x, y);
	}
	
	private int showBridgeValueDialog(){
		List<Integer> choices = new ArrayList<>();
		choices.add(1);
		choices.add(2);
		choices.add(3);
		choices.add(4);
		choices.add(5);
		choices.add(6);
		choices.add(7);
		choices.add(8);
		ChoiceDialog<Integer> dialog = new ChoiceDialog<Integer>(1, choices);
		dialog.setTitle("New Field");
		dialog.setHeaderText("Choos Field Value");
		dialog.setContentText("value");

		Optional<Integer> result = dialog.showAndWait();
		if (result.isPresent()) {
			return result.get();
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	void clickedOnField(FieldController field) {
		graphDas.removeField(field.getGraphField());
		addNewField(field.getGraphField().getX(), field.getGraphField().getY());
	}
	
	private void addNewField(int x, int y){
		int value = showBridgeValueDialog();
		graphDas.addField(new GraphField(x, y, value));
		logger.debug("Clicked on Game Field in Designer Mode. Add new Field at: x=" + x + " - y=" + y);
		UpdateThread updateThread = new UpdateThread(this, graphDas);
		isUpdating = true;
		updateThread.run();
	}
}
