package ch.ntb.swehashisg.hashi.model;

import java.util.Set;

public class GraphPlayField {

	private Set<GraphBridge> bridges;
	private Set<GraphBridge> solutionBridges;
	private Set<GraphField> fields;

	public GraphPlayField(Set<GraphBridge> bridges, Set<GraphBridge> solutionBridges, Set<GraphField> fields) {
		this.bridges = bridges;
		this.fields = fields;
		this.solutionBridges = solutionBridges;
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

}