package ch.ntb.swehashisg.hashi.graph;

import java.awt.List;
import java.util.ArrayList;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;

public class VersionedGraphDas extends AbstractGraphDas {
	
	public VersionedGraphDas(GraphDas gd) {
		mGraphDas = gd;
		mIndex = 0;
		mListOperations = new ArrayList<BridgeOperation>();
	}

	public boolean undo()
	{
		if(mIndex <= 0)
			return false;
		mIndex--;
		BridgeOperation bo = mListOperations.get(mIndex);
		executeOperation(!bo.mIsAddingBridge,bo.mGraphBridge);
		return true;
	}
	public boolean redo()
	{
		if(mIndex >= mListOperations.size() ||
		   mIndex < 0)
			return false;
		BridgeOperation bo = mListOperations.get(mIndex);
		executeOperation(bo.mIsAddingBridge, bo.mGraphBridge);		
		mIndex++;
		return true;
	}
	
	@Override
	public GraphPlayField getPlayField() {
		return mGraphDas.getPlayField();
	}

	@Override
	public void addBridge(GraphBridge bridge) {
		removeOperationsOverIndex();
		mListOperations.add( new BridgeOperation(true, bridge) );
		redo();
		
	}

	@Override
	public void removeBridge(GraphBridge bridge) {
		removeOperationsOverIndex();
		mListOperations.add( new BridgeOperation(false, bridge) );
		redo();
	}
	
	// Private Methods -----------------------------------------------
	private void removeOperationsOverIndex()
	{
		while(mListOperations.size() > mIndex) 
			mListOperations.remove(mIndex + 1);
	}
	
	private void executeOperation(boolean addBridge, GraphBridge bridge)
	{
		if(addBridge)
			mGraphDas.addBridge(bridge);
		else
			mGraphDas.removeBridge(bridge);
	}
	
	// Private Variables ----------------------------------------------
	private GraphDas mGraphDas;
	private class BridgeOperation extends Object
	{
		public BridgeOperation(boolean isAddingBridge, GraphBridge argGraphBridge)
		{
			mIsAddingBridge = isAddingBridge;
			mGraphBridge = argGraphBridge;
		}
		public boolean mIsAddingBridge;
		public GraphBridge mGraphBridge;
	}
	private ArrayList<BridgeOperation> mListOperations;
	private int mIndex;
}
