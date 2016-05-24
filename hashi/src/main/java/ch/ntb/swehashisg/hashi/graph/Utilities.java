package ch.ntb.swehashisg.hashi.graph;

import java.util.List;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.controller.Bridge;

public class Utilities {
	
	private static final Logger log = LoggerFactory.getLogger(Utilities.class);

	protected static Graph generateBasisPlayGround(Graph g) {
		Vertex play = g.addVertex("name", "test");
		Vertex root = null;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Vertex t = g.addVertex("x", i, "y", j, "bridges", 0);
				if (root == null) {
					root = t;
				}
				log.debug("j: " + j);
				log.debug("i: " + j);
				if (j > 0) {
					GraphTraversal<Vertex, Vertex> tr = g.traversal().V(root);
					if (i > 0) {
						tr = tr.repeat(__.out("row")).times(i);
					}
					if (j > 1) {
						tr = tr.repeat(__.out("column")).times(j - 1);
					}
					List<Vertex> parentColumns = tr.toList();
					if (parentColumns.size() != 1) {
						throw new IllegalArgumentException("No Parent Column found!");
					} else {
						parentColumns.get(0).addEdge("column", t);
					}
				}
				if (i > 0) {
					GraphTraversal<Vertex, Vertex> tr = g.traversal().V(root);
					if (i > 1) {
						tr = tr.repeat(__.out("row")).times(i - 1);
					}
					if (j > 0) {
						tr = tr.repeat(__.out("column")).times(j);
					}
					List<Vertex> parentColumns = tr.toList();
					if (parentColumns.size() != 1) {
						throw new IllegalArgumentException("No Parent Column found!");
					} else {
						parentColumns.get(0).addEdge("row", t);
					}
				}
			}
		}

		play.addEdge("field", root);

		return g;
	}

	public static void persistGraphDas(GraphDas g) {
		try {
			g.getGraph().io(IoCore.graphson()).writeGraph("test.json");
			g.getGraph().io(IoCore.graphml()).writeGraph("test.xml");
		} catch (Exception ex) {
			throw new RuntimeException("Couldn't persist Graph!", ex);
		}
	}

}
