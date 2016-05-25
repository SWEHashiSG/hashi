package ch.ntb.swehashisg.hashi.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;

public class BaseGraphDas extends GraphDas {

	private Graph graph;

	private HashMap<GraphBridge, Integer> bridgesToWeight;

	public BaseGraphDas(Graph graph) {
		this.graph = graph;
		this.bridgesToWeight = new HashMap<>();
	}

	protected void setBridges(GraphField field) {
		Vertex node = getVertexForField(field);
		node.property("bridges", field.getBridges());
	}

	public GraphPlayField getPlayField() {
		bridgesToWeight = new HashMap<>();
		Set<Vertex> vertices = getRelevantVertices();

		Set<GraphField> graphFields = convertVerticesToFields(vertices);

		for (Entry<GraphBridge, Integer> bridge : bridgesToWeight.entrySet()) {
			bridge.getKey().setWeighting(bridge.getValue() / 2);
		}

		GraphPlayField graphPlayField = new GraphPlayField(bridgesToWeight.keySet(), graphFields);
		return graphPlayField;
	}

	private Set<Vertex> getRelevantVertices() {
		Vertex root = graph.traversal().V().has("x", 0).has("y", 0).toList().get(0);

		Set<Vertex> vertices = new HashSet<>();
		graph.traversal().V(root).dedup().repeat(__.out("row", "column").sideEffect(t -> {
			if ((int) t.get().value("bridges") > 0)
				vertices.add(t.get());
		})).until(__.out("row", "column").count().is(0)).iterate();
		return vertices;
	}

	public boolean isCorrect() {
		Set<Vertex> vertices = getRelevantVertices();
		for (Vertex vertex : vertices) {
			int numberOfBridges = 0;
			Iterator<Edge> iterator = vertex.edges(Direction.BOTH, "bridge");
			while (iterator.hasNext()) {
				iterator.next();
				numberOfBridges++;
			}

			int neededBridges = vertex.value("bridges");
			if (neededBridges != numberOfBridges) {
				return false;
			}
		}

		return true;
	}

	@Override
	public int getSizeX() {
		return graph.traversal().V().has("name").toList().get(0).value("sizeX");
	}

	@Override
	public int getSizeY() {
		return graph.traversal().V().has("name").toList().get(0).value("sizeY");
	}

	private Set<GraphField> convertVerticesToFields(Set<Vertex> vertices) {
		Set<GraphField> fields = new HashSet<>();
		for (Vertex v : vertices) {
			fields.add(convertVertexToField(v));
		}
		return fields;
	}

	private Set<GraphField> convertVerticesToFieldsLight(Set<Vertex> vertices) {
		Set<GraphField> fields = new HashSet<>();
		for (Vertex v : vertices) {
			fields.add(convertVertexToFieldLight(v));
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
		Set<GraphField> neighborFields = convertVerticesToFieldsLight(neighbors);
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
		if (!areNeighbors2(node1, node2)) {
			throw new IllegalArgumentException("Crossing bridges!");
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
		return (int) node.property("bridges").value() > node.graph().traversal().V(node).bothE("bridge").count()
				.toList().get(0);
	}

	private Set<Vertex> getNeighbors(Vertex node) {
		Set<Vertex> vertices = new HashSet<>();
		Graph g = node.graph();

		vertices.addAll(g.traversal().V(node).repeat(__.in("row")).until(__.values("bridges").is(P.neq(0))).toSet());
		vertices.addAll(g.traversal().V(node).repeat(__.out("row")).until(__.values("bridges").is(P.neq(0))).toSet());
		vertices.addAll(g.traversal().V(node).repeat(__.in("column")).until(__.values("bridges").is(P.neq(0))).toSet());
		vertices.addAll(
				g.traversal().V(node).repeat(__.out("column")).until(__.values("bridges").is(P.neq(0))).toSet());
		Set<Vertex> realVertices = new HashSet<>();
		for (Vertex v : vertices) {
			if (areNeighbors2(node, v)) {
				realVertices.add(v);
			}
		}
		return realVertices;
	}

	private List<GraphBridge> getBridges(Vertex node) {
		List<GraphBridge> bridges = new ArrayList<>();
		Iterator<Edge> edges = node.edges(Direction.BOTH, "bridge");
		while (edges.hasNext()) {
			Edge edge = edges.next();
			GraphField node1 = convertVertexToFieldLight(edge.outVertex());
			GraphField node2 = convertVertexToFieldLight(edge.inVertex());
			GraphBridge bridge = new GraphBridge(node1, node2);
			if (bridgesToWeight.containsKey(bridge)) {
				bridgesToWeight.put(bridge, bridgesToWeight.get(bridge) + 1);
			} else {
				bridgesToWeight.put(bridge, 1);
			}
			bridges.add(bridge);
		}

		return bridges;
	}

	private boolean areNeighbors2(Vertex node1, Vertex node2) {
		int x1 = (int) node1.property("x").value();
		int y1 = (int) node1.property("y").value();
		int x2 = (int) node2.property("x").value();
		int y2 = (int) node2.property("y").value();
		Graph g = node1.graph();
		if (x1 != x2 && y1 != y2) {
			return false;
		} else {
			GraphTraversal<Vertex, Vertex> tr = g.traversal().V(node1);
			Predicate<Traverser<Vertex>> complexFilterColumn = new Predicate<Traverser<Vertex>>() {

				@Override
				public boolean test(Traverser<Vertex> t) {
					List<Long> testList = g.traversal().V(t.get())
							.match(__.<Vertex> as("t").repeat(__.in("row")).until(__.values("bridges").is(P.neq(0)))
									.bothE("bridge").as("bridge"),
									__.<Vertex> as("t").repeat(__.out("row")).until(__.values("bridges").is(P.neq(0)))
											.bothE("bridge").as("bridge"))
							.select("bridge").count().toList();
					if (testList.size() > 0) {
						if (testList.get(0) > 0) {
							return false;
						} else {
							return true;
						}
					} else {
						return true;
					}
				}
			};
			Predicate<Traverser<Vertex>> complexFilterRow = new Predicate<Traverser<Vertex>>() {

				@Override
				public boolean test(Traverser<Vertex> t) {
					List<Long> testList = g.traversal().V(t.get())
							.match(__.<Vertex> as("t").repeat(__.in("column")).until(__.values("bridges").is(P.neq(0)))
									.bothE("bridge").as("bridge"),
									__.<Vertex> as("t").repeat(__.out("column"))
											.until(__.values("bridges").is(P.neq(0))).bothE("bridge").as("bridge"))
							.select("bridge").count().toList();
					if (testList.size() > 0) {
						if (testList.get(0) > 0) {
							return false;
						} else {
							return true;
						}
					} else {
						return true;
					}
				}
			};
			if (x1 > x2) {
				tr = tr.repeat(__.in("row")
						.or(__.and(__.values("x").is(P.eq(x2)), __.values("bridges").is(P.neq(0))),
								__.and(__.values("x").is(P.gt(x2)), __.values("bridges").is(0)))
						.filter(complexFilterRow)).until(__.values("x").is(P.eq(x2)));
			}
			if (x1 < x2) {
				tr = tr.repeat(__.out("row")
						.or(__.and(__.values("x").is(P.eq(x2)), __.values("bridges").is(P.neq(0))),
								__.and(__.values("x").is(P.lt(x2)), __.values("bridges").is(0)))
						.filter(complexFilterRow)).until(__.values("x").is(P.eq(x2)));
			}
			if (y1 > y2) {
				tr = tr.repeat(__.in("column")
						.or(__.and(__.values("y").is(P.eq(y2)), __.values("bridges").is(P.neq(0))),
								__.and(__.values("y").is(P.gt(y2)), __.values("bridges").is(0)))
						.filter(complexFilterColumn)).until(__.values("y").is(P.eq(y2)));
			}
			if (y1 < y2) {
				tr = tr.repeat(__.out("column")
						.or(__.and(__.values("y").is(P.eq(y2)), __.values("bridges").is(P.neq(0))),
								__.and(__.values("y").is(P.lt(y2)), __.values("bridges").is(0)))
						.filter(complexFilterColumn)).until(__.values("y").is(P.eq(y2)));
			}
			return tr.toList().size() > 0;
		}
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
				tr = tr.repeat(__.in("row").or(__.and(__.values("x").is(P.eq(x2)), __.values("bridges").is(P.neq(0))),
						__.and(__.values("x").is(P.gt(x2)), __.values("bridges").is(0))))
						.until(__.values("x").is(P.eq(x2)));
			}
			if (x1 < x2) {
				tr = tr.repeat(__.out("row").or(__.and(__.values("x").is(P.eq(x2)), __.values("bridges").is(P.neq(0))),
						__.and(__.values("x").is(P.lt(x2)), __.values("bridges").is(0))))
						.until(__.values("x").is(P.eq(x2)));
			}
			if (y1 > y2) {
				tr = tr.repeat(
						__.in("column").or(__.and(__.values("y").is(P.eq(y2)), __.values("bridges").is(P.neq(0))),
								__.and(__.values("y").is(P.gt(y2)), __.values("bridges").is(0))))
						.until(__.values("y").is(P.eq(y2)));
			}
			if (y1 < y2) {
				tr = tr.repeat(
						__.out("column").or(__.and(__.values("y").is(P.eq(y2)), __.values("bridges").is(P.neq(0))),
								__.and(__.values("y").is(P.lt(y2)), __.values("bridges").is(0))))
						.until(__.values("y").is(P.eq(y2)));
			}
			return tr.toList().size() > 0;
		}
	}

	@Override
	void close() {
		try {
			graph.close();
		} catch (Exception e) {
			throw new RuntimeException("Couldn't close graph!");
		}
	}

	@Override
	Graph getGraph() {
		return this.graph;
	}

}
