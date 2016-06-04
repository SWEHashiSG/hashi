package ch.ntb.swehashisg.hashi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.graph.GraphDas;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 * Abstract controller class for the JavaFX view game field. The game field
 * extends a simple JavaFX Grid Pane where the fields and bridges are placed on
 * the screen. The methods are implemented in GameFieldPlayController for
 * playing the game and in GameFieldDesignerController to design own games.
 * 
 * @author Martin
 *
 */
public abstract class GameFieldController extends GridPane {

	private static final Logger logger = LoggerFactory.getLogger(GameFieldController.class);

	/**
	 * Controller of the main window where the game field will be placed.
	 */
	private MainWindowController mainWindowController;

	/**
	 * Model of the MVC Pattern where the data is saved
	 */
	protected GraphDas graphDas;

	/**
	 * A set of GraphFields which are all on the gameField
	 */
	protected Set<GraphField> graphFields;

	/**
	 * List of all FieldController which control the GraphFields
	 */
	protected ArrayList<FieldController> fields;

	/**
	 * Map to get the needed controller from a bridge
	 */
	protected HashMap<GraphBridge, BridgeController> graphBridgeToBridge;

	/**
	 * Map to get the needed controller from the highlighter of a bridge
	 */
	protected HashMap<GraphBridge, HighlightController> graphBridgeToHighlight;

	/**
	 * Map to get the solution controller of a bridge
	 */
	protected HashMap<GraphBridge, BridgeController> graphBridgeToSolutionBridge;

	/**
	 * is set during update the GraphDas model and redraw the game field
	 */
	protected boolean isUpdating = false;

	/**
	 * Constructor of the abstract class GameFieldController. Loads the
	 * FXML-file and placed it on the main Window. Sets the size of the game
	 * field and create all bridges and solution bridges
	 * 
	 * @param graphDas
	 *            Model of the MVC Pattern where the data is saved
	 * @param mainWindowController
	 *            Controller of the window where this game field will be placed
	 */
	public GameFieldController(GraphDas graphDas, MainWindowController mainWindowController) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GameField.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		this.graphDas = graphDas;
		this.mainWindowController = mainWindowController;
		setFieldSize(graphDas.getSizeX(), graphDas.getSizeY());
		GraphPlayField graphPlayField = graphDas.getPlayField();
		graphFields = graphPlayField.getFields();
		createAllBridges(getBridges(graphPlayField));
		createAllSolutions(graphPlayField.getSolutionBridges());
	}

	/**
	 * Create all solution bridges on the game field and placed it invisible on
	 * the screen
	 * 
	 * @param solutionBridges
	 */
	private void createAllSolutions(Set<GraphBridge> solutionBridges) {
		graphBridgeToSolutionBridge = new HashMap<>();
		for (GraphBridge bridge : solutionBridges) {
			graphBridgeToSolutionBridge.put(bridge, new BridgeController(bridge, this, false));
		}
	}

	/**
	 * From Template Pattern. Abstract getBridges method so the implementation
	 * of this class can decide which bridges will be returned;
	 * 
	 * @param graphPlayField
	 *            data model from GraphDas
	 * @return bridge choose by the implementation
	 */
	protected abstract Set<GraphBridge> getBridges(GraphPlayField graphPlayField);

	/**
	 * Create all bridges and the associated controllers and add them to the
	 * maps and lists
	 * 
	 * @param bridges
	 */
	private void createAllBridges(Set<GraphBridge> bridges) {
		graphBridgeToBridge = new HashMap<>();
		for (GraphBridge bridge : bridges) {
			graphBridgeToBridge.put(bridge, new BridgeController(bridge, this));
		}
		HashMap<GraphField, GraphField> lightFieldToRealField = new HashMap<>();
		for (GraphField field : graphFields) {
			lightFieldToRealField.put(field, field);
		}
		graphBridgeToHighlight = new HashMap<>();
		for (GraphField field : graphFields) {
			for (GraphField neighbor : field.getNeighbors()) {
				GraphField fullNeighbor = lightFieldToRealField.get(neighbor);
				GraphBridge newBridge = new GraphBridge(field, fullNeighbor);

				if (!bridges.contains(newBridge)) {
					graphBridgeToBridge.put(newBridge, new BridgeController(newBridge, this));
				}
				graphBridgeToHighlight.put(newBridge, new HighlightController(field, fullNeighbor, this));
			}
		}
	}

	/**
	 * update Method will be called after every change on the game
	 * 
	 * @param graphPlayField
	 *            new data modell from the background system
	 */
	protected void update(GraphPlayField graphPlayField) {
		graphFields = graphPlayField.getFields();
		createAllBridges(getBridges(graphPlayField));
		loadGame();
		isUpdating = false;
	}

	/**
	 * initiate the grid pane from the JavaFX. Add columns and rows and set its
	 * size
	 * 
	 * @param sizeX
	 *            horizontal size of the game
	 * @param sizeY
	 *            vertical size of the game
	 */
	private void setFieldSize(int sizeX, int sizeY) {
		getRowConstraints().clear();
		getColumnConstraints().clear();
		for (int i = 0; i < sizeY; i++) {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPrefHeight(FieldController.getFieldSize());
			getRowConstraints().add(rowConstraints);
		}
		for (int i = 0; i < sizeX; i++) {
			ColumnConstraints columnConstraints = new ColumnConstraints();
			columnConstraints.setPrefWidth(FieldController.getFieldSize());
			getColumnConstraints().add(columnConstraints);
		}
	}

	/**
	 * clear the exisiting game field and all all fields, bridges, highliter,
	 * and solution bridges on the game
	 */
	public void loadGame() {
		logger.debug("Draw all Fields");
		cleanGameField();
		fields = new ArrayList<FieldController>();
		for (GraphField graphField : graphFields) {
			FieldController field = new FieldController(graphField, this);
			field.addToGameField();
			fields.add(field);
		}
		for (HighlightController highlight : graphBridgeToHighlight.values()) {
			highlight.addToGameField();
		}
		for (BridgeController bridge : graphBridgeToBridge.values()) {
			bridge.addToGameField();
		}
		for (BridgeController solution : graphBridgeToSolutionBridge.values()) {
			solution.addToGameField();
		}
	}

	/**
	 * cleaning the game field by deleting all children of the grid pane. With
	 * one exception, because the visibility of the grid lines are also saved in
	 * the children of the grid pane
	 */
	private void cleanGameField() {
		if (!getChildren().isEmpty()) {
			Node n = getChildren().get(0);
			getChildren().clear();
			if (n instanceof Group) {
				logger.debug("Grid Lines are Visible");
				getChildren().add(n);
			}
		}
	}

	HighlightController getNorthHighlight(FieldController field) {
		return graphBridgeToHighlight
				.get(new GraphBridge(field.getGraphField(), field.getGraphField().getNorthNeighbor()));
	}

	HighlightController getEastHighlight(FieldController field) {
		return graphBridgeToHighlight
				.get(new GraphBridge(field.getGraphField(), field.getGraphField().getEastNeighbor()));
	}

	HighlightController getSouthHighlight(FieldController field) {
		return graphBridgeToHighlight
				.get(new GraphBridge(field.getGraphField(), field.getGraphField().getSouthNeighbor()));
	}

	HighlightController getWestHighlight(FieldController field) {
		return graphBridgeToHighlight
				.get(new GraphBridge(field.getGraphField(), field.getGraphField().getWestNeighbor()));
	}

	boolean hasBridge(GraphField neighbor1, GraphField neighbor2) {
		return graphBridgeToBridge.containsKey(new GraphBridge(neighbor1, neighbor2));
	}

	GraphBridge getBridge(GraphField neighbor1, GraphField neighbor2) {
		return graphBridgeToBridge.get(new GraphBridge(neighbor1, neighbor2)).getGraphBridge();
	}

	/**
	 * take both neighbors and compare the number of existing bridges and the
	 * number of bridges needed
	 * 
	 * @param highlight
	 * @return true if the highlight needs a Bridge
	 */
	boolean needsBridge(HighlightController highlight) {
		GraphBridge bridge = new GraphBridge(highlight.getNeighbor1(), highlight.getNeighbor2());
		int weighting = graphBridgeToBridge.get(bridge).getGraphBridge().getWeighting();
		if (highlight.getNeighbor1().getBridges() > highlight.getNeighbor1().getExistingBridges().size()
				&& highlight.getNeighbor2().getBridges() > highlight.getNeighbor2().getExistingBridges().size()) {
			if (weighting < 2) {
				return true;
			}
		}
		return false;
	}

	/**
	 * remove the bridges were the highlight is associated to. If there are two
	 * bridges, both will be deleted
	 * 
	 * @param highlight
	 *            which associated to the bridge
	 */
	public void removeBridge(HighlightController highlight) {
		if (!isUpdating) {
			GraphBridge bridge = new GraphBridge(highlight.getNeighbor1(), highlight.getNeighbor2());
			if (graphBridgeToBridge.get(bridge).getGraphBridge().getWeighting() == 2) {
				removeBridge(bridge);
			}
			removeBridge(bridge);
			initiateUpdate();
		} else {
			logger.warn("Is already updating, better solution needed!");
		}
	}

	/**
	 * adding a new Bridge to the game Field.
	 * 
	 * @param highlight
	 */
	public void addBridge(HighlightController highlight) {
		if (!isUpdating) {
			GraphBridge bridge = new GraphBridge(highlight.getNeighbor1(), highlight.getNeighbor2());
			addBridge(bridge);
			initiateUpdate();
		} else {
			logger.warn("Is already updating, better solution needed!");
		}
	}

	protected abstract void addBridge(GraphBridge graphBridge);

	protected abstract void removeBridge(GraphBridge graphBridge);

	/**
	 * Abstract Method which will be called if the user clicked on the empty
	 * Pane. Normally it does nothing but can be overwritten in the
	 * implementation
	 * 
	 * @param event
	 */
	@FXML
	protected void clickedOnPane(MouseEvent event){
		/* Does nothing */
	}

	/**
	 * This method will be called form the FieldController when the user clicks
	 * on its field. Normally it does nothing but can be overwritten in the
	 * implementation
	 * 
	 * @param field
	 *            controller of the field where the user has clicked
	 */
	protected void clickedOnField(FieldController field) {
		/* Does nothing */
	}

	/**
	 * Initiate the Update of the gameField when something has changed. Create a
	 * new UpdateThread which is doing the work so the GUI will not be blocked
	 */
	protected void initiateUpdate() {
		logger.debug("-------------Redraw whole gamefield-----------------");
		UpdateThread updateThread = new UpdateThread(this, graphDas, mainWindowController);
		isUpdating = true;
		updateThread.run();
	}

	/**
	 * private class to Update the gameField in a own thread, that the GUI is
	 * not blocked
	 * 
	 * @author Martin
	 *
	 */
	static class UpdateThread extends Thread {

		private GameFieldController gameField;
		private GraphDas graphDas;
		private MainWindowController mainWindowController;

		public UpdateThread(GameFieldController gameField, GraphDas graphDas,
				MainWindowController mainWindowController) {
			this.gameField = gameField;
			this.graphDas = graphDas;
			this.mainWindowController = mainWindowController;
		}

		@Override
		public void run() {
			GraphPlayField playField = graphDas.getPlayField();
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					mainWindowController.updateButtons(graphDas);
					gameField.update(playField);
				}
			});
		}
	}

	/**
	 * toggling the visibility of the solutionBridges and of the actual Bridges
	 * on the Field
	 */
	public void showSolution() {
		for (BridgeController solutionBridge : graphBridgeToSolutionBridge.values()) {
			solutionBridge.toggleVisibility();
		}
		for (BridgeController bridge : graphBridgeToBridge.values()) {
			bridge.toggleVisibility();
		}
	}

	public void undo() {
		if (graphDas.canUndo()) {
			graphDas.undo();
			initiateUpdate();
		}
	}

	public void redo() {
		if (graphDas.canRedo()) {
			graphDas.redo();
			initiateUpdate();
		}
	}

	public void restart() {
		graphDas.restart();
		initiateUpdate();
	}

	/**
	 * Check all bridges if they are correct. compare if for all bridges there
	 * is a solution bridge and the solution doesn't have a higher weighting If
	 * a fault is found, the bridge will be marked.
	 * 
	 * @return true if game is correct or false if a fault was found
	 */
	public boolean isCorrect() {
		boolean isCorrect = true;
		if (graphDas.isFinished()) {
			return isCorrect;
		} else {
			for (GraphBridge bridge : graphDas.getPlayField().getBridges()) {
				BridgeController solutionController = graphBridgeToSolutionBridge.get(bridge);
				if (solutionController == null) {
					markFaultBridge(graphBridgeToBridge.get(bridge));
					isCorrect = false;
				} else if (solutionController.getGraphBridge().getWeighting() < bridge.getWeighting()) {
					markFaultBridge(graphBridgeToBridge.get(bridge));
					isCorrect = false;
				}
			}
		}
		return isCorrect;
	}

	/**
	 * Mark a fault bridge with a Red Background
	 * 
	 * @param bridgeController
	 *            controller of the graphBridge which is not correct
	 */
	private void markFaultBridge(BridgeController bridgeController) {
		logger.warn("You have placed a wrong Bridge");
		graphBridgeToHighlight.get(bridgeController.getGraphBridge()).markRed();
	}
}
