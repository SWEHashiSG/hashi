package ch.ntb.swehashisg.hashi.graph;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;

public class VersionedGraphDas extends AbstractGraphDas {
	
	public VersionedGraphDas(GraphDas gd) {
		mGraphDas = gd;
	}

	public void undo()
	{
		
	}
	public void redo()
	{
		
	}
	
	@Override
	public GraphPlayField getPlayField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addBridge(GraphBridge bridge) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeBridge(GraphBridge bridge) {
		// TODO Auto-generated method stub

	}
	
	
	// Private -----------------------------------------------
	private GraphDas mGraphDas;
}
