package ch.ntb.swehashisg.hashi.graph;

import java.io.File;
import java.io.IOException;

import org.apache.tinkerpop.gremlin.neo4j.structure.Neo4jGraph;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.neo4j.io.fs.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.controller.GraphPersistence;

/**
 * Provides methods for getting, loading and persisting a graphService. There
 * will be only one graphService allow to be used at once. This class is not
 * thread safe.
 *
 */
public class GraphServiceFactory {

	private static final Logger logger = LoggerFactory.getLogger(GraphServiceFactory.class);

	private static Neo4jGraph actualGraph;

	/**
	 * Gets an graphService, with an example graph
	 * 
	 * @return the example graphService
	 */
	public static GraphService getGraphService() {
		closePreviousGraph();
		Neo4jGraph ne = Neo4jGraph.open("./neo4j");
		actualGraph = ne;
		Graph g = ne;

		g = Utilities.generateBasisPlayGround(g, 8, 8);

		BaseGraphDas graphDas = new BaseGraphDas(g);

		graphDas = GraphInitializer.generateExamplePlay(graphDas);

		GraphService graphService = new BaseGraphService(graphDas);

		return new VersionedGraphService(graphService);
	}

	/**
	 * Closes the graphServices
	 * 
	 * @param graphService
	 *            the graphService to close
	 */
	public static void closeGraphService(GraphService graphService) {
		try {
			actualGraph.close();
		} catch (Exception e) {
			throw new RuntimeException("Couldn't close graph!");
		}
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

	/**
	 * Generate graphService with a graph of the provided size
	 * 
	 * @param sizeX
	 *            number of rows
	 * @param sizeY
	 *            number of columns
	 * @return the graphService with the generated graph
	 */
	public static GraphService getEmptyGraphService(int sizeX, int sizeY) {
		closePreviousGraph();
		Neo4jGraph ne = Neo4jGraph.open("./neo4j");
		actualGraph = ne;
		Graph g = ne;
		g = Utilities.generateBasisPlayGround(g, sizeX, sizeY);
		BaseGraphDas graphDas = new BaseGraphDas(g);
		GraphService graphService = new BaseGraphService(graphDas);

		return new VersionedGraphService(graphService);
	}

	/**
	 * Persist the, with the graphService associated graph, where
	 * graphPersistence provides the format and place to use.
	 * 
	 * @param graphService
	 *            the graphService to take the graph from
	 * @param graphPersistence
	 *            provides the format and place
	 */
	public static void persistGraphService(GraphService graphService, GraphPersistence graphPersistence) {
		try {
			if (graphPersistence.getGraphFormat() == GraphFormat.XML) {
				actualGraph.io(IoCore.graphml()).writeGraph(graphPersistence.getPath());
			} else if (graphPersistence.getGraphFormat() == GraphFormat.JSON) {
				actualGraph.io(IoCore.graphson()).writeGraph(graphPersistence.getPath());
			} else {
				throw new IllegalArgumentException("Unknown GraphFormat: " + graphPersistence.getGraphFormat());
			}
		} catch (Exception ex) {
			throw new RuntimeException("Couldn't persist Graph!", ex);
		}
	}

	/**
	 * Loads a existing graph, where graphPersistence provides the format and
	 * place to use.
	 * 
	 * @param graphPersistence
	 *            provides the format and place
	 * @return a graphService with the loaded graph
	 */
	public static GraphService loadGraphService(GraphPersistence graphPersistence) {
		try {
			closePreviousGraph();
			Neo4jGraph ne = Neo4jGraph.open("./neo4j");
			actualGraph = ne;
			if (graphPersistence.getGraphFormat() == GraphFormat.XML) {
				ne.io(IoCore.graphml()).readGraph(graphPersistence.getPath());
			} else if (graphPersistence.getGraphFormat() == GraphFormat.JSON) {
				ne.io(IoCore.graphson()).readGraph(graphPersistence.getPath());
			} else {
				throw new IllegalArgumentException("Unknown GraphFormat: " + graphPersistence.getGraphFormat());
			}
			Graph g = ne;
			BaseGraphDas graphDas = new BaseGraphDas(g);
			GraphService graphService = new BaseGraphService(graphDas);

			return new VersionedGraphService(graphService);
		} catch (Exception ex) {
			throw new RuntimeException("Couldn't read Graph!", ex);
		}
	}

}
