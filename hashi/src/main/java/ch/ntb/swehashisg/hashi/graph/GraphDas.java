package ch.ntb.swehashisg.hashi.graph;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;

public interface GraphDas {
	public GraphPlayField getPlayField();

	public void addBridge(GraphBridge bridge);

	public void removeBridge(GraphBridge bridge);

	public boolean isCorrect();
}
