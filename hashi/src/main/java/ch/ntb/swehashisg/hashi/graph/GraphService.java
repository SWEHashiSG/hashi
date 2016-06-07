package ch.ntb.swehashisg.hashi.graph;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;

public interface GraphService {

	GraphPlayField getPlayField();

	void setBridges(GraphField field);

	boolean isFinished(GraphPlayField graphPlayField);

	void addBridge(GraphBridge bridge);

	void addSolutionBridge(GraphBridge bridge);

	void removeBridge(GraphBridge bridge);

	void removeSolutionBridge(GraphBridge bridge);

	void undo(GraphPlayField graphPlayField);

	boolean canUndo();

	void redo(GraphPlayField graphPlayField);

	boolean canRedo();

	void restart();

}