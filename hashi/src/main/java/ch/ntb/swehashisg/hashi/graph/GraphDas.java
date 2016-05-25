package ch.ntb.swehashisg.hashi.graph;

import org.apache.tinkerpop.gremlin.structure.Graph;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;

public abstract class GraphDas {
	public abstract GraphPlayField getPlayField();

	public abstract void addBridge(GraphBridge bridge);

	public abstract void removeBridge(GraphBridge bridge);

	public abstract boolean isCorrect();

	abstract void close();

	abstract Graph getGraph();
}
