package ch.ntb.swehashisg.hashi.graph;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

public class GraphDasFactory {

	public static BaseGraphDas getGraphDas() {
		TinkerGraph tg = TinkerGraph.open();
		tg.createIndex("x", Vertex.class);
		tg.createIndex("y", Vertex.class);
		Graph g = tg;

		g = Utilities.generateBasisPlayGround(g);

		BaseGraphDas graphDas = new BaseGraphDas(g);

		graphDas = GraphInitializer.generateExamplePlay(graphDas);

		return graphDas;
	}

	public static void closeGraphDas(BaseGraphDas graphDas) {
		graphDas.close();
	}

}
