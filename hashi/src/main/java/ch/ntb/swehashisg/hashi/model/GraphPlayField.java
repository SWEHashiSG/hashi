package ch.ntb.swehashisg.hashi.model;

import java.util.HashMap;
import java.util.Set;

public class GraphPlayField {

	private HashMap<GraphBridge, GraphBridge> bridges;
	private HashMap<GraphBridge, GraphBridge> solutionBridges;
	private HashMap<GraphField, GraphField> fields;
	private int sizeX;
	private int sizeY;

	public GraphPlayField(Set<GraphBridge> bridges, Set<GraphBridge> solutionBridges, Set<GraphField> fields, int sizeX,
			int sizeY) {
		this.bridges = new HashMap<>();
		for (GraphBridge graphBridge : bridges) {
			this.bridges.put(graphBridge, graphBridge);
		}
		this.solutionBridges = new HashMap<>();
		for (GraphBridge graphBridge : solutionBridges) {
			this.solutionBridges.put(graphBridge, graphBridge);
		}
		this.fields = new HashMap<>();
		for (GraphField graphField : fields) {
			this.fields.put(graphField, graphField);
		}
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	public GraphBridge getBridge(GraphBridge graphBridge) {
		return bridges.get(graphBridge);
	}

	public Set<GraphBridge> getBridges() {
		return bridges.keySet();
	}

	public GraphBridge getSolutionBridge(GraphBridge graphBridge) {
		return solutionBridges.get(graphBridge);
	}

	public Set<GraphBridge> getSolutionBridges() {
		return solutionBridges.keySet();
	}

	public GraphField getField(GraphField graphField) {
		return fields.get(graphField);
	}

	public Set<GraphField> getFields() {
		return fields.keySet();
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

}