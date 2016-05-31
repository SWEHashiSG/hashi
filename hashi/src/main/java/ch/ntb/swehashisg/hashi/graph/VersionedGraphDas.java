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

	public boolean undo() {
		if (index <= 0)
			return false;
		index--;
		BridgeOperation bo = listOperations.get(index);
		executeOperation(!bo.isAddingBridge, bo.graphBridge);
		return true;
	}

	public boolean redo() {
		if (index >= listOperations.size() || index < 0)
			return false;
		BridgeOperation bo = listOperations.get(index);
		executeOperation(bo.isAddingBridge, bo.graphBridge);
		index++;
		return true;
	}

	@Override
	public GraphPlayField getPlayField() {
		return graphDas.getPlayField();
	}

	@Override
	public void addBridge(GraphBridge bridge) {
		removeOperationsOverIndex();
		listOperations.add(new BridgeOperation(true, bridge));
		redo();

	}

	@Override
	public void removeBridge(GraphBridge bridge) {
		removeOperationsOverIndex();
		listOperations.add(new BridgeOperation(false, bridge));
		redo();
	}

	@Override
	public boolean isCorrect() {
		return graphDas.isCorrect();
	}

	private void removeOperationsOverIndex() {
		while (listOperations.size() > index)
			listOperations.remove(index + 1);
	}

	private void executeOperation(boolean addBridge, GraphBridge bridge) {
		if (addBridge)
			graphDas.addBridge(bridge);
		else
			graphDas.removeBridge(bridge);
	}

	private static class BridgeOperation extends Object {

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
