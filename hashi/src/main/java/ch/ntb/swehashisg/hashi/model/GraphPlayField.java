package ch.ntb.swehashisg.hashi.model;

import java.util.Set;

public class GraphPlayField {

	private Set<GraphBridge> bridges;

	private Set<GraphField> fields;

	public GraphPlayField(Set<GraphBridge> bridges, Set<GraphField> fields) {
		this.bridges = bridges;
		this.fields = fields;
	}

	public Set<GraphBridge> getBridges() {
		return bridges;
	}

	public Set<GraphField> getFields() {
		return fields;
	}

}
