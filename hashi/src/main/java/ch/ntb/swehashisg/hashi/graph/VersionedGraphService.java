package ch.ntb.swehashisg.hashi.graph;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.controller.MainWindowController;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;

/**
 * Provides a decorator for graphService, so that operations can be undone, redone.
 *
 */
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

	/*
	 * (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#canUndo()
	 */
	@Override
	public boolean canUndo() {
		return !undoOperations.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#undo()
	 */
	@Override
	public void undo(GraphPlayField graphPlayField) {
		if (!canUndo()) {
			throw new IllegalArgumentException("Nothing to undo!");
		}
		logger.debug("undo");
		GraphServiceOperation graphServiceOperation = undoOperations.pop();
		redoOperations.push(graphServiceOperation);
		graphServiceOperation.undo(graphPlayField);
	}

	/*
	 * (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#canRedo()
	 */
	@Override
	public boolean canRedo() {
		return !redoOperations.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#redo()
	 */
	@Override
	public void redo(GraphPlayField graphPlayField) {
		if (!canRedo()) {
			throw new IllegalArgumentException("Nothing to redo!");
		}
		GraphServiceOperation graphServiceOperation = redoOperations.pop();
		undoOperations.push(graphServiceOperation);
		graphServiceOperation.redo(graphPlayField);
	}

	private void addOperation(GraphServiceOperation bridgeOperation) {
		undoOperations.push(bridgeOperation);
	}

	/*
	 * (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#getPlayField()
	 */
	@Override
	public GraphPlayField getPlayField() {
		return graphService.getPlayField();
	}

	/*
	 * (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#addBridge(ch.ntb.swehashisg.hashi.model.GraphBridge)
	 */
	@Override
	public void addBridge(GraphBridge bridge) {
		graphService.addBridge(bridge);
		addOperation(new AddBridgeOperation(bridge, graphService));
		removeNewerOperation();
	}

	/*
	 * (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#removeBridge(ch.ntb.swehashisg.hashi.model.GraphBridge)
	 */
	@Override
	public void removeBridge(GraphBridge bridge) {
		graphService.removeBridge(bridge);
		addOperation(new RemoveBridgeOperation(bridge, graphService));
		removeNewerOperation();
	}

	/*
	 * (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#isFinished(ch.ntb.swehashisg.hashi.model.GraphPlayField)
	 */
	@Override
	public boolean isFinished(GraphPlayField graphPlayField) {
		return graphService.isFinished(graphPlayField);
	}

	private void removeNewerOperation() {
		redoOperations.clear();
	}

	private static interface GraphServiceOperation {
		public void undo(GraphPlayField graphPlayField);

		public void redo(GraphPlayField graphPlayField);
	}

	private static class AddBridgeOperation implements GraphServiceOperation {
		private GraphBridge graphBridge;
		private GraphService graphService;

		public AddBridgeOperation(GraphBridge graphBridge, GraphService graphService) {
			this.graphBridge = graphBridge;
			this.graphService = graphService;
		}

		@Override
		public void undo(GraphPlayField graphPlayField) {
			GraphBridge actualGraphBridge = getActualBridge(graphPlayField);
			graphService.removeBridge(actualGraphBridge);
		}

		@Override
		public void redo(GraphPlayField graphPlayField) {
			GraphBridge actualGraphBridge = getActualBridge(graphPlayField);
			graphService.addBridge(actualGraphBridge);
		}

		private GraphBridge getActualBridge(GraphPlayField graphPlayField) {
			GraphField actualField1 = graphPlayField.getField(graphBridge.getField1());
			GraphField actualField2 = graphPlayField.getField(graphBridge.getField2());
			GraphBridge actualGraphBridge = new GraphBridge(actualField1, actualField2);
			return actualGraphBridge;
		}
	}

	private static class RemoveBridgeOperation implements GraphServiceOperation {
		private AddBridgeOperation addBridgeOperation;

		public RemoveBridgeOperation(GraphBridge graphBridge, GraphService graphService) {
			this.addBridgeOperation = new AddBridgeOperation(graphBridge, graphService);

		}

		@Override
		public void undo(GraphPlayField graphPlayField) {
			addBridgeOperation.redo(graphPlayField);
		}

		@Override
		public void redo(GraphPlayField graphPlayField) {
			addBridgeOperation.undo(graphPlayField);
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
		public void undo(GraphPlayField graphPlayField) {
			GraphBridge actualGraphBridge = getActualBridge(graphPlayField);
			
			graphService.removeSolutionBridge(actualGraphBridge);
		}

		@Override
		public void redo(GraphPlayField graphPlayField) {
			GraphBridge actualGraphBridge = getActualBridge(graphPlayField);

			graphService.addSolutionBridge(actualGraphBridge);
		}
		
		private GraphBridge getActualBridge(GraphPlayField graphPlayField) {
			GraphField actualField1 = graphPlayField.getField(graphBridge.getField1());
			GraphField actualField2 = graphPlayField.getField(graphBridge.getField2());
			GraphBridge actualGraphBridge = new GraphBridge(actualField1, actualField2);
			return actualGraphBridge;
		}
	}

	private static class RemoveSolutionBridgeOperation implements GraphServiceOperation {
		private AddSolutionBridgeOperation addSolutionBridgeOperation;

		public RemoveSolutionBridgeOperation(GraphBridge graphBridge, GraphService graphService) {
			this.addSolutionBridgeOperation = new AddSolutionBridgeOperation(graphBridge, graphService);

		}

		@Override
		public void undo(GraphPlayField graphPlayField) {
			addSolutionBridgeOperation.redo(graphPlayField);
		}

		@Override
		public void redo(GraphPlayField graphPlayField) {
			addSolutionBridgeOperation.undo(graphPlayField);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#setBridges(ch.ntb.swehashisg.hashi.model.GraphField)
	 */
	@Override
	public void setBridges(GraphField field) {
		graphService.setBridges(field);
	}

	/*
	 * (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#addSolutionBridge(ch.ntb.swehashisg.hashi.model.GraphBridge)
	 */
	@Override
	public void addSolutionBridge(GraphBridge bridge) {
		graphService.addSolutionBridge(bridge);
		addOperation(new AddSolutionBridgeOperation(bridge, graphService));
	}

	/*
	 * (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#removeSolutionBridge(ch.ntb.swehashisg.hashi.model.GraphBridge)
	 */
	@Override
	public void removeSolutionBridge(GraphBridge bridge) {
		graphService.removeSolutionBridge(bridge);
		addOperation(new RemoveSolutionBridgeOperation(bridge, graphService));
	}

	/*
	 * (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#restart(ch.ntb.swehashisg.hashi.model.GraphPlayField)
	 */
	@Override
	public void restart(GraphPlayField graphPlayField) {
		graphService.restart(graphPlayField);
		undoOperations.clear();
		redoOperations.clear();
	}
}
