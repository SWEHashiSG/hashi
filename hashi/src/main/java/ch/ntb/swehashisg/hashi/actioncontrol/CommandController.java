package ch.ntb.swehashisg.hashi.actioncontrol;

import org.apache.tinkerpop.gremlin.structure.Graph;

import ch.ntb.swehashisg.hashi.graph.TestGraph;
import ch.ntb.swehashisg.hashi.graph.TestGraph.Bridge;

public class CommandController {
	public CommandController(TestGraph tg)
	{
		mTestGraph = tg;
	}
	
	public void addBridge(Bridge bridge, Graph g)
	{
		mTestGraph.addBridge(bridge, g);
	}
	public void removeBridge(Bridge bridge, Graph g)
	{
		mTestGraph.removeBridge(bridge, g);
	}
	public void undo()
	{
		
	}
	public void redo()
	{
		
	}
	
	
	private TestGraph mTestGraph;
}
