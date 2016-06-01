package ch.ntb.swehashisg.hashi.graph;

import org.apache.tinkerpop.gremlin.structure.Graph;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;

public abstract class GraphDas {
	public abstract GraphPlayField getPlayField();

	public abstract void addBridge(GraphBridge bridge);

	public abstract void setBridges(GraphField field);

	public abstract void removeBridge(GraphBridge bridge);

	public abstract boolean isCorrect();

	abstract void close();

	abstract Graph getGraph();

	public abstract int getSizeX();

	public abstract int getSizeY();

	public abstract void undo();

	public abstract boolean canUndo();

	public abstract void redo();

	public abstract boolean canRedo();

	public abstract void addSolutionBridge(GraphBridge bridge);

	public abstract void removeSolutionBridge(GraphBridge bridge);

	public abstract void restart();
}
