package ch.ntb.swehashisg.hashi.graph;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;

public abstract class AbstractGraphDas {
	abstract public GraphPlayField getPlayField();
	abstract public void addBridge(GraphBridge bridge);
	abstract public void removeBridge(GraphBridge bridge);
}
