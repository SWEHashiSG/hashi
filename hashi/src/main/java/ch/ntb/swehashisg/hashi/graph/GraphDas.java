package ch.ntb.swehashisg.hashi.graph;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;

public interface GraphDas {

	public GraphPlayField getPlayField();

	public void addBridge(GraphBridge bridge);

	public void setBridges(GraphField field);

	public void removeBridge(GraphBridge bridge);

	public boolean isFinished();

	public int getSizeX();

	public int getSizeY();

	public void undo();

	public boolean canUndo();

	public void redo();

	public boolean canRedo();

	public void addSolutionBridge(GraphBridge bridge);

	public void removeSolutionBridge(GraphBridge bridge);

	public void restart();
}
