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

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;

public class GraphDas {

	private Graph graph;

	public GraphDas(Graph graph) {
		this.graph = graph;
	}

	protected void setBridges(GraphField field) {
		Vertex node = getVertexForField(field);
		node.property("bridges", field.getBridges());
	}

	public Set<GraphField> getRelevantFields() {
		Vertex root = graph.traversal().V().has("x", 0).has("y", 0).toList().get(0);

		Set<Vertex> vertices = new HashSet<>();
		graph.traversal().V(root).dedup().repeat(__.out("row", "column").sideEffect(t -> {
			if ((int) t.get().value("bridges") > 0)
				vertices.add(t.get());
		})).until(__.out("row", "column").count().is(0)).iterate();
		return convertVerticesToFields(vertices);
	}

	private Set<GraphField> convertVerticesToFields(Set<Vertex> vertices) {
		Set<GraphField> fields = new HashSet<>();
		for (Vertex v : vertices) {
			fields.add(convertVertexToField(v));
		}
		return fields;
	}

	private Vertex getVertexForField(GraphField field) {
		return graph.traversal().V().has("x", field.getX()).has("y", field.getY()).toList().get(0);
	}

	private GraphField convertVertexToField(Vertex v) {
		int x = (int) v.property("x").value();
		int y = (int) v.property("y").value();
		int bridges = (int) v.property("bridges").value();
		Set<Vertex> neighbors = getNeighbors(v);
		Set<GraphField> neighborFields = convertVerticesToFields(neighbors);
		List<GraphBridge> existingBridges = getBridges(v);
		return new GraphField(x, y, bridges, neighborFields, existingBridges);
	}

	private GraphField convertVertexToFieldLight(Vertex v) {
		int x = (int) v.property("x").value();
		int y = (int) v.property("y").value();
		int bridges = (int) v.property("bridges").value();
		return new GraphField(x, y, bridges);
	}

	public void addBridge(GraphBridge bridge) {
		Vertex node1 = graph.traversal().V().has("x", bridge.getField1().getX()).has("y", bridge.getField1().getY())
				.toList().get(0);
		Vertex node2 = graph.traversal().V().has("x", bridge.getField2().getX()).has("y", bridge.getField2().getY())
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

	public void removeBridge(GraphBridge bridge) {
		Vertex node1 = graph.traversal().V().has("x", bridge.getField1().getX()).has("y", bridge.getField1().getY())
				.toList().get(0);
		Vertex node2 = graph.traversal().V().has("x", bridge.getField2().getX()).has("y", bridge.getField2().getY())
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

	private boolean needsBridge(Vertex node) {
		return (long) node.property("bridges").value() >= node.graph().traversal().V(node).bothE("bridge").count()
				.toList().get(0);
	}

	private Set<Vertex> getNeighbors(Vertex node) {
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

	private List<GraphBridge> getBridges(Vertex node) {
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

	private boolean areNeighbors(Vertex node1, Vertex node2) {
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

	void close() {
		try {
			graph.close();
		} catch (Exception e) {
			throw new RuntimeException("Couldn't close graph!");
		}
	}

	Graph getGraph() {
		return this.graph;
	}

}
