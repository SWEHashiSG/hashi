package ch.ntb.swehashisg.hashi.graph;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;

public class BaseGraphService implements GraphService {

	private GraphDas graphDas;

	BaseGraphService(GraphDas graphDas) {
		this.graphDas = graphDas;
	}

	/* (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#getPlayField()
	 */
	@Override
	public GraphPlayField getPlayField() {
		return graphDas.getPlayField();
	}

	/* (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#setBridges(ch.ntb.swehashisg.hashi.model.GraphField)
	 */
	@Override
	public void setBridges(GraphField field) {
		graphDas.setBridges(field);
	}

	/* (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#isFinished(ch.ntb.swehashisg.hashi.model.GraphPlayField)
	 */
	@Override
	public boolean isFinished(GraphPlayField graphPlayField) {
		for (GraphField field : graphPlayField.getFields()) {

			int numberOfBridges = 0;
			for (GraphBridge graphBridge : field.getExistingBridges()) {
				numberOfBridges += graphBridge.getWeighting();
			}
			if (field.getBridges() != numberOfBridges) {
				return false;
			}
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#addBridge(ch.ntb.swehashisg.hashi.model.GraphBridge)
	 */
	@Override
	public void addBridge(GraphBridge bridge) {
		addGenericBridge(BridgeType.NORMAL, bridge);
	}

	/* (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#addSolutionBridge(ch.ntb.swehashisg.hashi.model.GraphBridge)
	 */
	@Override
	public void addSolutionBridge(GraphBridge bridge) {
		addGenericBridge(BridgeType.SOLUTION, bridge);
	}

	private void addGenericBridge(BridgeType bridgeType, GraphBridge bridge) {
		if (!needsBridge(bridgeType, bridge.getField1())) {
			throw new IllegalArgumentException("Doesn't need bridge!");
		}
		if (!needsBridge(bridgeType, bridge.getField2())) {
			throw new IllegalArgumentException("Doesn't need bridge!");
		}
		if (!areNeighbors(bridge.getField1(), bridge.getField2())) {
			throw new IllegalArgumentException("Crossing bridges!");
		}
		if (bridgeType == BridgeType.NORMAL) {
			graphDas.addBridge(bridge);
		} else {
			graphDas.addSolutionBridge(bridge);
		}
	}

	private boolean needsBridge(BridgeType bridgeType, GraphField graphField) {
		if (bridgeType == BridgeType.NORMAL) {
			return graphField.getBridges() != getNumberOfBridges(graphField);
		} else {
			return graphField.getBridges() != getNumberOfSolutionBridges(graphField);
		}
	}

	private int getNumberOfBridges(GraphField graphField) {
		int numberOfBridges = 0;
		for (GraphBridge graphBridge : graphField.getExistingBridges()) {
			numberOfBridges += graphBridge.getWeighting();
		}
		return numberOfBridges;
	}

	private int getNumberOfSolutionBridges(GraphField graphField) {
		int numberOfBridges = 0;
		for (GraphBridge graphBridge : graphField.getExistingSolutionBridges()) {
			numberOfBridges += graphBridge.getWeighting();
		}
		return numberOfBridges;
	}

	/* (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#removeBridge(ch.ntb.swehashisg.hashi.model.GraphBridge)
	 */
	@Override
	public void removeBridge(GraphBridge bridge) {
		removeGenericBridge(BridgeType.NORMAL, bridge);
	}

	/* (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#removeSolutionBridge(ch.ntb.swehashisg.hashi.model.GraphBridge)
	 */
	@Override
	public void removeSolutionBridge(GraphBridge bridge) {
		removeGenericBridge(BridgeType.SOLUTION, bridge);
	}

	private void removeGenericBridge(BridgeType bridgeType, GraphBridge bridge) {
		if (!areNeighbors(bridge.getField1(), bridge.getField2())) {
			throw new IllegalArgumentException("Need to be neighbors!");
		}
		GraphBridge graphBridge = getCandidateBridge(bridgeType, bridge.getField1(), bridge.getField2());
		if (graphBridge == null) {
			throw new IllegalArgumentException("No " + bridgeType.getLabel() + " to delete found!");
		} else {
			graphDas.removeBridge(graphBridge);
		}
	}

	private GraphBridge getCandidateBridge(BridgeType bridgeType, GraphField field1, GraphField field2) {
		for (GraphBridge graphBridge : field1.getExistingBridges()) {
			if (graphBridge.getField1().equals(field2) || graphBridge.getField2().equals(field2)) {
				return graphBridge;
			}
		}

		return null;
	}

	private static enum BridgeType {
		SOLUTION("solutionBridge"), NORMAL("bridge");
		String label;

		private BridgeType(String label) {
			this.label = label;
		}

		public String getLabel() {
			return this.label;
		}
	}

	private boolean areNeighbors(GraphField node1, GraphField node2) {
		return node1.getNeighbors().contains(node2);
	}

	/* (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#undo()
	 */
	@Override
	public void undo(GraphPlayField graphPlayField) {
		throw new UnsupportedOperationException("undo function is not Implemented in BaseGraphDas");
	}

	/* (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#canUndo()
	 */
	@Override
	public boolean canUndo() {
		throw new UnsupportedOperationException("canUndo function is not Implemented in BaseGraphDas");
	}

	/* (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#redo()
	 */
	@Override
	public void redo(GraphPlayField graphPlayField) {
		throw new UnsupportedOperationException("redo function is not Implemented in BaseGraphDas");
	}

	/* (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#canRedo()
	 */
	@Override
	public boolean canRedo() {
		throw new UnsupportedOperationException("canRedo function is not Implemented in BaseGraphDas");
	}

	/* (non-Javadoc)
	 * @see ch.ntb.swehashisg.hashi.graph.GraphService#restart()
	 */
	@Override
	public void restart(GraphPlayField graphPlayField) {
		for (GraphBridge bridge : graphPlayField.getBridges()) {
			for (int i = 0; i < bridge.getWeighting(); i++) {
				graphDas.removeBridge(bridge);
			}
		}
	}

}
