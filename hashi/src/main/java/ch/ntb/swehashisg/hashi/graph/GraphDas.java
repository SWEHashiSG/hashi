package ch.ntb.swehashisg.hashi.graph;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;

public interface GraphDas {

	public GraphPlayField getPlayField();

	public void addBridge(GraphBridge bridge);

	public void setBridges(GraphField field);

	public void removeBridge(GraphBridge bridge);

	public void addSolutionBridge(GraphBridge bridge);

	public void removeSolutionBridge(GraphBridge bridge);
}
