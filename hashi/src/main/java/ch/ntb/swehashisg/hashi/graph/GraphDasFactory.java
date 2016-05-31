package ch.ntb.swehashisg.hashi.graph;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

public class GraphDasFactory {

	public static GraphDas getGraphDas() {
		TinkerGraph tg = TinkerGraph.open();
		tg.createIndex("x", Vertex.class);
		tg.createIndex("y", Vertex.class);
		Graph g = tg;

		g = Utilities.generateBasisPlayGround(g, 8, 8);

		BaseGraphDas graphDas = new BaseGraphDas(g);

		graphDas = GraphInitializer.generateExamplePlay(graphDas);

		return new VersionedGraphDas(graphDas);
	}

	public static void closeGraphDas(GraphDas graphDas) {
		graphDas.close();
	}

	public static GraphDas loadGraphDas(String absolutpath, GraphFormat graphFormat) {
		try {
			TinkerGraph tg = TinkerGraph.open();
			if (graphFormat == GraphFormat.XML) {
				tg.io(IoCore.graphml()).readGraph(absolutpath);
			} else if (graphFormat == GraphFormat.JSON) {
				tg.io(IoCore.graphson()).readGraph(absolutpath);
			} else {
				throw new IllegalArgumentException("Unknown GraphFormat: " + graphFormat);
			}
			tg.createIndex("x", Vertex.class);
			tg.createIndex("y", Vertex.class);
			Graph g = tg;
			BaseGraphDas graphDas = new BaseGraphDas(g);
			return new VersionedGraphDas(graphDas);
		} catch (Exception ex) {
			throw new RuntimeException("Couldn't read Graph!", ex);
		}
	}

	public static GraphDas getEmptyGraphDas(int sizeX, int sizeY) {
		TinkerGraph tg = TinkerGraph.open();
		tg.createIndex("x", Vertex.class);
		tg.createIndex("y", Vertex.class);
		Graph g = tg;
		g = Utilities.generateBasisPlayGround(g,sizeX, sizeY);
		BaseGraphDas graphDas = new BaseGraphDas(g);
		return new VersionedGraphDas(graphDas);
	}

}
