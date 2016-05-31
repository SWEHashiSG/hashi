package ch.ntb.swehashisg.hashi.graph;

import java.util.ArrayList;

import org.apache.tinkerpop.gremlin.structure.Graph;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;

public class VersionedGraphDas extends GraphDas {

	private BaseGraphDas graphDas;
	private ArrayList<BridgeOperation> listOperations;
	private int index;

	public VersionedGraphDas(BaseGraphDas gd) {
		this.graphDas = gd;
		this.index = 0;
		this.listOperations = new ArrayList<BridgeOperation>();
	}

	public boolean canUndo() {
		return (index > 0);
	}

	public void undo() {
		if (!canUndo()) {
			throw new IllegalArgumentException("Nothing to undo!");
		}

		index--;
		BridgeOperation bo = listOperations.get(index);
		executeOperation(!bo.isAddingBridge, bo.graphBridge);
	}

	public boolean canRedo() {
		return !((index >= listOperations.size() || index < 0));
	}

	public void redo() {
		if (!canRedo()) {
			throw new IllegalArgumentException("Nothing to redo!");
		}
		BridgeOperation bo = listOperations.get(index);
		executeOperation(bo.isAddingBridge, bo.graphBridge);
		index++;
	}

	private void addOperation(BridgeOperation bridgeOperation) {
		listOperations.add(bridgeOperation);
		index++;
	}

	@Override
	public GraphPlayField getPlayField() {
		return graphDas.getPlayField();
	}

	@Override
	public void addBridge(GraphBridge bridge) {
		graphDas.addBridge(bridge);
		addOperation(new BridgeOperation(true, bridge));
	}

	@Override
	public void removeBridge(GraphBridge bridge) {
		graphDas.removeBridge(bridge);
		addOperation(new BridgeOperation(false, bridge));
	}

	@Override
	public boolean isCorrect() {
		return graphDas.isCorrect();
	}

	private void executeOperation(boolean addBridge, GraphBridge bridge) {
		if (addBridge)
			graphDas.addBridge(bridge);
		else
			graphDas.removeBridge(bridge);
	}

	private static class BridgeOperation {

		public boolean isAddingBridge;
		public GraphBridge graphBridge;

		public BridgeOperation(boolean isAddingBridge, GraphBridge graphBridge) {
			this.isAddingBridge = isAddingBridge;
			this.graphBridge = graphBridge;
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
}
