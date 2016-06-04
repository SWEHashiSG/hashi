package ch.ntb.swehashisg.hashi.graph;

import java.util.Stack;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.controller.MainWindowController;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;

public class VersionedGraphDas extends GraphDas {

	private static final Logger logger = LoggerFactory.getLogger(MainWindowController.class);

	private BaseGraphDas graphDas;
	private Stack<GraphDasOperation> undoOperations;
	private Stack<GraphDasOperation> redoOperations;

	public VersionedGraphDas(BaseGraphDas gd) {
		this.graphDas = gd;
		undoOperations = new Stack<>();
		redoOperations = new Stack<>();
	}

	public boolean canUndo() {
		return !undoOperations.isEmpty();
	}

	public void undo() {
		if (!canUndo()) {
			throw new IllegalArgumentException("Nothing to undo!");
		}
		logger.debug("undo");
		GraphDasOperation graphDasOperation = undoOperations.pop();
		redoOperations.push(graphDasOperation);
		graphDasOperation.undo();
	}

	public boolean canRedo() {
		return !redoOperations.isEmpty();
	}

	public void redo() {
		if (!canRedo()) {
			throw new IllegalArgumentException("Nothing to redo!");
		}
		GraphDasOperation graphDasOperation = redoOperations.pop();
		undoOperations.push(graphDasOperation);
		graphDasOperation.redo();
	}

	private void addOperation(GraphDasOperation bridgeOperation) {
		undoOperations.push(bridgeOperation);
	}

	@Override
	public GraphPlayField getPlayField() {
		return graphDas.getPlayField();
	}

	@Override
	public void addBridge(GraphBridge bridge) {
		graphDas.addBridge(bridge);
		addOperation(new AddBridgeOperation(bridge, graphDas));
		removeNewerOperation();
	}

	@Override
	public void removeBridge(GraphBridge bridge) {
		graphDas.removeBridge(bridge);
		addOperation(new RemoveBridgeOperation(bridge, graphDas));
		removeNewerOperation();
	}

	@Override
	public boolean isFinished() {
		return graphDas.isFinished();
	}

	private void removeNewerOperation() {
		redoOperations.clear();
	}

	private static interface GraphDasOperation {
		public void undo();

		public void redo();
	}

	private static class AddBridgeOperation implements GraphDasOperation {
		private GraphBridge graphBridge;
		private GraphDas graphDas;

		public AddBridgeOperation(GraphBridge graphBridge, GraphDas graphDas) {
			this.graphBridge = graphBridge;
			this.graphDas = graphDas;
		}

		@Override
		public void undo() {
			logger.debug("@AddBridgeOperation::undo();------------------------");
			graphDas.removeBridge(graphBridge);
		}

		@Override
		public void redo() {
			graphDas.addBridge(graphBridge);
		}
	}

	private static class RemoveBridgeOperation implements GraphDasOperation {
		private AddBridgeOperation addBridgeOperation;

		public RemoveBridgeOperation(GraphBridge graphBridge, GraphDas graphDas) {
			this.addBridgeOperation = new AddBridgeOperation(graphBridge, graphDas);

		}

		@Override
		public void undo() {
			addBridgeOperation.redo();
		}

		@Override
		public void redo() {
			addBridgeOperation.undo();
		}
	}

	private static class AddSolutionBridgeOperation implements GraphDasOperation {
		private GraphBridge graphBridge;
		private GraphDas graphDas;

		public AddSolutionBridgeOperation(GraphBridge graphBridge, GraphDas graphDas) {
			this.graphBridge = graphBridge;
			this.graphDas = graphDas;
		}

		@Override
		public void undo() {
			logger.debug("@AddSolutionBridgeOperation::undo();------------------------");
			graphDas.removeSolutionBridge(graphBridge);
		}

		@Override
		public void redo() {
			graphDas.addSolutionBridge(graphBridge);
		}
	}

	private static class RemoveSolutionBridgeOperation implements GraphDasOperation {
		private AddSolutionBridgeOperation addSolutionBridgeOperation;

		public RemoveSolutionBridgeOperation(GraphBridge graphBridge, GraphDas graphDas) {
			this.addSolutionBridgeOperation = new AddSolutionBridgeOperation(graphBridge, graphDas);

		}

		@Override
		public void undo() {
			addSolutionBridgeOperation.redo();
		}

		@Override
		public void redo() {
			addSolutionBridgeOperation.undo();
		}
	}

	@Override
	void close() {
		graphDas.close();
	}

	@Override
	Graph getGraph() {
		return graphDas.getGraph();
	}

	@Override
	public int getSizeX() {
		return graphDas.getSizeX();
	}

	@Override
	public int getSizeY() {
		return graphDas.getSizeY();
	}

	@Override
	public void setBridges(GraphField field) {
		graphDas.setBridges(field);
	}

	@Override
	public void addSolutionBridge(GraphBridge bridge) {
		graphDas.addSolutionBridge(bridge);
		addOperation(new AddSolutionBridgeOperation(bridge, graphDas));
	}

	@Override
	public void removeSolutionBridge(GraphBridge bridge) {
		graphDas.removeSolutionBridge(bridge);
		addOperation(new RemoveSolutionBridgeOperation(bridge, graphDas));
	}

	@Override
	public void restart() {
		graphDas.restart();
		undoOperations.clear();
		redoOperations.clear();
	}
}
