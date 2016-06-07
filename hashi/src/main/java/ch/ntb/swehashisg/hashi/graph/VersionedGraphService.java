package ch.ntb.swehashisg.hashi.graph;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.controller.MainWindowController;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;

public class VersionedGraphService implements GraphService {

	private static final Logger logger = LoggerFactory.getLogger(MainWindowController.class);

	private GraphService graphService;
	private Stack<GraphServiceOperation> undoOperations;
	private Stack<GraphServiceOperation> redoOperations;

	VersionedGraphService(GraphService gd) {
		this.graphService = gd;
		undoOperations = new Stack<>();
		redoOperations = new Stack<>();
	}

	@Override
	public boolean canUndo() {
		return !undoOperations.isEmpty();
	}

	@Override
	public void undo() {
		if (!canUndo()) {
			throw new IllegalArgumentException("Nothing to undo!");
		}
		logger.debug("undo");
		GraphServiceOperation graphServiceOperation = undoOperations.pop();
		redoOperations.push(graphServiceOperation);
		graphServiceOperation.undo();
	}

	@Override
	public boolean canRedo() {
		return !redoOperations.isEmpty();
	}

	@Override
	public void redo() {
		if (!canRedo()) {
			throw new IllegalArgumentException("Nothing to redo!");
		}
		GraphServiceOperation graphServiceOperation = redoOperations.pop();
		undoOperations.push(graphServiceOperation);
		graphServiceOperation.redo();
	}

	private void addOperation(GraphServiceOperation bridgeOperation) {
		undoOperations.push(bridgeOperation);
	}

	@Override
	public GraphPlayField getPlayField() {
		return graphService.getPlayField();
	}

	@Override
	public void addBridge(GraphBridge bridge) {
		graphService.addBridge(bridge);
		addOperation(new AddBridgeOperation(bridge, graphService));
		removeNewerOperation();
	}

	@Override
	public void removeBridge(GraphBridge bridge) {
		graphService.removeBridge(bridge);
		addOperation(new RemoveBridgeOperation(bridge, graphService));
		removeNewerOperation();
	}

	@Override
	public boolean isFinished(GraphPlayField graphPlayField) {
		return graphService.isFinished(graphPlayField);
	}

	private void removeNewerOperation() {
		redoOperations.clear();
	}

	private static interface GraphServiceOperation {
		public void undo();

		public void redo();
	}

	private static class AddBridgeOperation implements GraphServiceOperation {
		private GraphBridge graphBridge;
		private GraphService graphService;

		public AddBridgeOperation(GraphBridge graphBridge, GraphService graphService) {
			this.graphBridge = graphBridge;
			this.graphService = graphService;
		}

		@Override
		public void undo() {
			logger.debug("@AddBridgeOperation::undo();------------------------");
			graphService.removeBridge(graphBridge);
		}

		@Override
		public void redo() {
			graphService.addBridge(graphBridge);
		}
	}

	private static class RemoveBridgeOperation implements GraphServiceOperation {
		private AddBridgeOperation addBridgeOperation;

		public RemoveBridgeOperation(GraphBridge graphBridge, GraphService graphService) {
			this.addBridgeOperation = new AddBridgeOperation(graphBridge, graphService);

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

	private static class AddSolutionBridgeOperation implements GraphServiceOperation {
		private GraphBridge graphBridge;
		private GraphService graphService;

		public AddSolutionBridgeOperation(GraphBridge graphBridge, GraphService graphService) {
			this.graphBridge = graphBridge;
			this.graphService = graphService;
		}

		@Override
		public void undo() {
			logger.debug("@AddSolutionBridgeOperation::undo();------------------------");
			graphService.removeSolutionBridge(graphBridge);
		}

		@Override
		public void redo() {
			graphService.addSolutionBridge(graphBridge);
		}
	}

	private static class RemoveSolutionBridgeOperation implements GraphServiceOperation {
		private AddSolutionBridgeOperation addSolutionBridgeOperation;

		public RemoveSolutionBridgeOperation(GraphBridge graphBridge, GraphService graphService) {
			this.addSolutionBridgeOperation = new AddSolutionBridgeOperation(graphBridge, graphService);

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
	public void setBridges(GraphField field) {
		graphService.setBridges(field);
	}

	@Override
	public void addSolutionBridge(GraphBridge bridge) {
		graphService.addSolutionBridge(bridge);
		addOperation(new AddSolutionBridgeOperation(bridge, graphService));
	}

	@Override
	public void removeSolutionBridge(GraphBridge bridge) {
		graphService.removeSolutionBridge(bridge);
		addOperation(new RemoveSolutionBridgeOperation(bridge, graphService));
	}

	@Override
	public void restart() {
		graphService.restart();
		undoOperations.clear();
		redoOperations.clear();
	}
}
