package ch.ntb.swehashisg.hashi.graph;

import java.io.File;
import java.io.IOException;

import org.apache.tinkerpop.gremlin.neo4j.structure.Neo4jGraph;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.neo4j.io.fs.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphDasFactory {

	private static final Logger logger = LoggerFactory.getLogger(GraphDasFactory.class);

	private static Neo4jGraph actualGraph;

	public static GraphDas getGraphDas() {
		closePreviousGraph();
		Neo4jGraph ne = Neo4jGraph.open("./neo4j");
		actualGraph = ne;
		Graph g = ne;

		g = Utilities.generateBasisPlayGround(g, 8, 8);

		BaseGraphDas graphDas = new BaseGraphDas(g);

		graphDas = GraphInitializer.generateExamplePlay(graphDas);

		return new VersionedGraphDas(graphDas);
	}

	public static void closeGraphDas(GraphDas graphDas) {
		graphDas.close();
	}

	private static void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				closePreviousGraph();
			}
		});
	}

	private static void closePreviousGraph() {
		if (actualGraph != null) {
			try {
				actualGraph.close();
				actualGraph.getBaseGraph().shutdown();
				logger.info("Old Graph closed");
			} catch (Exception ex) {
				throw new RuntimeException("Couldn't close previous graph!");
			}
		} else {
			registerShutdownHook();
		}
		try {
			FileUtils.deleteRecursively(new File("./neo4j"));
		} catch (IOException e) {
			logger.error("Could not clear database");
		}
	}

	public static GraphDas loadGraphDas(String absolutpath, GraphFormat graphFormat) {
		try {
			closePreviousGraph();
			Neo4jGraph ne = Neo4jGraph.open("./neo4j");
			actualGraph = ne;
			if (graphFormat == GraphFormat.XML) {
				ne.io(IoCore.graphml()).readGraph(absolutpath);
			} else if (graphFormat == GraphFormat.JSON) {
				ne.io(IoCore.graphson()).readGraph(absolutpath);
			} else {
				throw new IllegalArgumentException("Unknown GraphFormat: " + graphFormat);
			}
			Graph g = ne;
			BaseGraphDas graphDas = new BaseGraphDas(g);
			return new VersionedGraphDas(graphDas);
		} catch (Exception ex) {
			throw new RuntimeException("Couldn't read Graph!", ex);
		}
	}

	public static GraphDas getEmptyGraphDas(int sizeX, int sizeY) {
		closePreviousGraph();
		Neo4jGraph ne = Neo4jGraph.open("./neo4j");
		actualGraph = ne;
		Graph g = ne;
		g = Utilities.generateBasisPlayGround(g, sizeX, sizeY);
		BaseGraphDas graphDas = new BaseGraphDas(g);
		return new VersionedGraphDas(graphDas);
	}

}
