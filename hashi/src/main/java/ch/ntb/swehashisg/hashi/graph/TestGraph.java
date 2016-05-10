package ch.ntb.swehashisg.hashi.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;

public class TestGraph {

	public static void main(String[] args) {

		try {
			TinkerGraph tg = TinkerGraph.open();
			tg.createIndex("x", Vertex.class);
			tg.createIndex("y", Vertex.class);
			Graph g = tg;

			g = generateBasisPlayGround(g);

			g = generateExamplePlay(g);

			getRelevantFields(g);

			g.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
		System.exit(0);
	}

	private static Graph generateBasisPlayGround(Graph g) {
		Vertex play = g.addVertex("name", "test");
		Vertex root = null;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Vertex t = g.addVertex("x", i, "y", j, "bridges", 0);
				if (root == null) {
					root = t;
				}
				System.out.println("j: " + j);
				System.out.println("i: " + j);
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

	private static Graph generateExamplePlay(Graph g) {
		setBridges(new GraphField(0, 1, 1), g);
		setBridges(new GraphField(0, 3, 2), g);
		setBridges(new GraphField(0, 5, 3), g);
		setBridges(new GraphField(0, 7, 1), g);

		setBridges(new GraphField(1, 0, 3), g);
		setBridges(new GraphField(1, 2, 5), g);
		setBridges(new GraphField(1, 4, 3), g);

		setBridges(new GraphField(2, 5, 4), g);
		setBridges(new GraphField(2, 7, 1), g);

		setBridges(new GraphField(3, 2, 4), g);
		setBridges(new GraphField(3, 4, 4), g);

		setBridges(new GraphField(4, 0, 4), g);
		setBridges(new GraphField(4, 3, 5), g);
		setBridges(new GraphField(4, 5, 8), g);
		setBridges(new GraphField(4, 7, 3), g);

		setBridges(new GraphField(5, 6, 1), g);

		setBridges(new GraphField(6, 0, 3), g);
		setBridges(new GraphField(6, 2, 1), g);
		setBridges(new GraphField(6, 7, 1), g);
		setBridges(new GraphField(6, 5, 2), g);

		setBridges(new GraphField(7, 1, 1), g);
		setBridges(new GraphField(7, 3, 4), g);
		setBridges(new GraphField(7, 6, 2), g);

		return g;
	}

	public static void persistGraph(Graph g) {
		try {
			g.io(IoCore.graphson()).writeGraph("test.json");
			g.io(IoCore.graphml()).writeGraph("test.xml");
		} catch (Exception ex) {
			throw new RuntimeException("Couldn't persist Graph!", ex);
		}
	}

	private static void setBridges(GraphField field, Graph g) {
		Vertex node = getVertexForField(field, g);
		node.property("bridges", field.getBridges());
	}

	public static Set<GraphField> getRelevantFields(Graph g) {
		Vertex root = g.traversal().V().has("x", 0).has("y", 0).toList().get(0);

		Set<Vertex> vertices = new HashSet<>();
		g.traversal().V(root).dedup().repeat(__.out("row", "column").sideEffect(t -> {
			if ((int) t.get().value("bridges") > 0)
				vertices.add(t.get());
		})).until(__.out("row", "column").count().is(0)).iterate();
		return convertVerticesToFields(vertices);
	}

	private static Set<GraphField> convertVerticesToFields(Set<Vertex> vertices) {
		Set<GraphField> fields = new HashSet<>();
		for (Vertex v : vertices) {
			fields.add(convertVertexToField(v));
		}
		return fields;
	}

	private static Vertex getVertexForField(GraphField field, Graph g) {
		return g.traversal().V().has("x", field.getX()).has("y", field.getY()).toList().get(0);
	}

	private static GraphField convertVertexToField(Vertex v) {
		int x = (int) v.property("x").value();
		int y = (int) v.property("y").value();
		int bridges = (int) v.property("bridges").value();
		Set<Vertex> neighbors = getNeighbors(v);
		Set<GraphField> neighborFields = convertVerticesToFields(neighbors);
		List<GraphBridge> existingBridges = getBridges(v);
		return new GraphField(x, y, bridges, neighborFields, existingBridges);
	}

	private static GraphField convertVertexToFieldLight(Vertex v) {
		int x = (int) v.property("x").value();
		int y = (int) v.property("y").value();
		int bridges = (int) v.property("bridges").value();
		return new GraphField(x, y, bridges);
	}

	public static void addBridge(GraphBridge bridge, Graph g) {
		Vertex node1 = g.traversal().V().has("x", bridge.getField1().getX()).has("y", bridge.getField1().getY())
				.toList().get(0);
		Vertex node2 = g.traversal().V().has("x", bridge.getField2().getX()).has("y", bridge.getField2().getY())
				.toList().get(0);
		if (!needsBridge(node1)) {
			throw new IllegalArgumentException("Doesn't need bridge!");
		}
		if (!needsBridge(node2)) {
			throw new IllegalArgumentException("Doesn't need bridge!");
		}
		if (!areNeighbors(node1, node2)) {
			throw new IllegalArgumentException("Need to be neighbors!");
		}
		node1.addEdge("bridge", node2);
	}

	public static void removeBridge(GraphBridge bridge, Graph g) {
		Vertex node1 = g.traversal().V().has("x", bridge.getField1().getX()).has("y", bridge.getField1().getY())
				.toList().get(0);
		Vertex node2 = g.traversal().V().has("x", bridge.getField2().getX()).has("y", bridge.getField2().getY())
				.toList().get(0);
		if (!areNeighbors(node1, node2)) {
			throw new IllegalArgumentException("Need to be neighbors!");
		}
		List<Object> possibleCandidates = node1.graph().traversal().V(node1).outE("bridge").as("edgeToDelete").bothV()
				.is(node2).select("edgeToDelete").toList();
		if (possibleCandidates.size() == 0) {
			throw new IllegalArgumentException("No bridge to delete found!");
		} else {
			Edge e = (Edge) possibleCandidates.get(0);
			e.remove();
		}
	}

	private static boolean needsBridge(Vertex node) {
		return (long) node.property("bridges").value() >= node.graph().traversal().V(node).bothE("bridge").count()
				.toList().get(0);
	}

	private static Set<Vertex> getNeighbors(Vertex node) {
		Set<Vertex> vertices = new HashSet<>();
		Graph g = node.graph();
		vertices.addAll(g.traversal().V(node).repeat(__.in("row"))
				.until(__.values("bridges").is((Predicate<Object>) t -> (int) t != 0)).toSet());
		vertices.addAll(g.traversal().V(node).repeat(__.out("row"))
				.until(__.values("bridges").is((Predicate<Object>) t -> (int) t != 0)).toSet());
		vertices.addAll(g.traversal().V(node).repeat(__.in("column"))
				.until(__.values("bridges").is((Predicate<Object>) t -> (int) t != 0)).toSet());
		vertices.addAll(g.traversal().V(node).repeat(__.out("column"))
				.until(__.values("bridges").is((Predicate<Object>) t -> (int) t != 0)).toSet());
		return vertices;
	}

	private static List<GraphBridge> getBridges(Vertex node) {
		List<GraphBridge> bridges = new ArrayList<>();
		Iterator<Edge> edges = node.edges(Direction.BOTH, "bridge");
		while (edges.hasNext()) {
			Edge edge = edges.next();
			GraphField node1 = convertVertexToFieldLight(edge.inVertex());
			GraphField node2 = convertVertexToFieldLight(edge.inVertex());
			GraphBridge bridge = new GraphBridge(node1, node2);
			bridges.add(bridge);
		}

		return bridges;
	}

	private static boolean areNeighbors(Vertex node1, Vertex node2) {
		int x1 = (int) node1.property("x").value();
		int y1 = (int) node1.property("y").value();
		int x2 = (int) node2.property("x").value();
		int y2 = (int) node2.property("y").value();
		Graph g = node1.graph();
		if (x1 != x2 && y1 != y2) {
			return false;
		} else {
			GraphTraversal<Vertex, Vertex> tr = g.traversal().V(node1);
			if (x1 > x2) {
				tr = tr.repeat(__.in("row").and(__.values("bridges").is(0))).until(__.values("x").is(x2));
			}
			if (x1 < x2) {
				tr = tr.repeat(__.out("row").and(__.values("bridges").is(0))).until(__.values("x").is(x2));
			}
			if (y1 > y2) {
				tr = tr.repeat(__.in("column").and(__.values("bridges").is(0))).until(__.values("y").is(y2));
			}
			if (y1 < y2) {
				tr = tr.repeat(__.out("column").and(__.values("bridges").is(0))).until(__.values("y").is(y2));
			}
			return tr.toList().size() > 0;
		}
	}

}
