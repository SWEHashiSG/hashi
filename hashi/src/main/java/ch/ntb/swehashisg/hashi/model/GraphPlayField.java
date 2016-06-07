package ch.ntb.swehashisg.hashi.model;

import java.util.Set;

public class GraphPlayField {

	private Set<GraphBridge> bridges;
	private Set<GraphBridge> solutionBridges;
	private Set<GraphField> fields;
	private int sizeX;
	private int sizeY;

	public GraphPlayField(Set<GraphBridge> bridges, Set<GraphBridge> solutionBridges, Set<GraphField> fields, int sizeX, int sizeY) {
		this.bridges = bridges;
		this.fields = fields;
		this.solutionBridges = solutionBridges;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	public Set<GraphBridge> getBridges() {
		return bridges;
	}

	public Set<GraphBridge> getSolutionBridges() {
		return solutionBridges;
	}

	public Set<GraphField> getFields() {
		return fields;
	}
	
	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

}