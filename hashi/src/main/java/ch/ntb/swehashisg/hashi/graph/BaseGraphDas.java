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

public class BaseGraphDas implements GraphDas {

	private Graph graph;

	private HashMap<GraphBridge, Integer> bridgesToWeight;
	private HashMap<GraphBridge, Integer> solutionBridgesToWeight;

	BaseGraphDas(Graph graph) {
		this.graph = graph;
		this.bridgesToWeight = new HashMap<>();
		this.solutionBridgesToWeight = new HashMap<>();
	}

	@Override
	public GraphPlayField getPlayField() {
		bridgesToWeight = new HashMap<>();
		solutionBridgesToWeight = new HashMap<>();
		Set<Vertex> vertices = getRelevantVertices();

		Set<GraphField> graphFields = convertVerticesToFields(vertices);

		correctWeightingOfBridges();

		GraphPlayField graphPlayField = new GraphPlayField(bridgesToWeight.keySet(), solutionBridgesToWeight.keySet(),
				graphFields);
		return graphPlayField;
	}

	private void correctWeightingOfBridges() {
		for (Entry<GraphBridge, Integer> bridge : bridgesToWeight.entrySet()) {
			bridge.getKey().setWeighting(bridge.getValue() / 2);
		}

		for (Entry<GraphBridge, Integer> bridge : solutionBridgesToWeight.entrySet()) {
			bridge.getKey().setWeighting(bridge.getValue() / 2);
		}
	}

	private Set<Vertex> getRelevantVertices() {
		Vertex root = getRootField();

		Set<Vertex> vertices = new HashSet<>();
		graph.traversal().V(root).dedup().repeat(__.out("row", "column").sideEffect(t -> {
			if ((int) t.get().value("bridges") > 0)
				vertices.add(t.get());
		})).until(__.out("row", "column").count().is(0)).iterate();
		if ((int) root.value("bridges") > 0) {
			vertices.add(root);
		}
		return vertices;
	}

	private Vertex getRootField() {
		return graph.traversal().V().has("x", 0).has("y", 0).toList().get(0);
	}

	@Override
	public void setBridges(GraphField field) {
		Vertex node = getVertexForField(field);
		node.property("bridges", field.getBridges());
	}
	
	@Override
	public boolean isFinished() {
		Set<Vertex> vertices = getRelevantVertices();
		for (Vertex vertex : vertices) {
			int numberOfBridges = getNumberOfBridges(vertex);

			int neededBridges = vertex.value("bridges");
			if (neededBridges != numberOfBridges) {
				return false;
			}
		}

		return true;
	}

	private int getNumberOfBridges(Vertex v) {
		int numberOfBridges = 0;
		Iterator<Edge> iterator = v.edges(Direction.BOTH, "bridge");
		while (iterator.hasNext()) {
			iterator.next();
			numberOfBridges++;
		}
		return numberOfBridges;
	}

	@Override
	public int getSizeX() {
		return graph.traversal().V().has("name", "test").toList().get(0).value("sizeX");
	}

	@Override
	public int getSizeY() {
		return graph.traversal().V().has("name", "test").toList().get(0).value("sizeY");
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
		List<GraphBridge> existingSolutionBridges = getSolutionBridges(v);
		return new GraphField(x, y, bridges, neighborFields, existingBridges, existingSolutionBridges);
	}

	private GraphField convertVertexToFieldLight(Vertex v) {
		int x = (int) v.property("x").value();
		int y = (int) v.property("y").value();
		int bridges = (int) v.property("bridges").value();
		return new GraphField(x, y, bridges);
	}

	@Override
	public void addBridge(GraphBridge bridge) {
		addGenericBridge(BridgeType.NORMAL, bridge);
	}

	@Override
	public void addSolutionBridge(GraphBridge bridge) {
		addGenericBridge(BridgeType.SOLUTION, bridge);
	}

	private void addGenericBridge(BridgeType bridgeType, GraphBridge bridge) {
		Vertex node1 = getVertexForField(bridge.getField1());
		Vertex node2 = getVertexForField(bridge.getField2());
		if (!needsBridge(bridgeType, node1)) {
			throw new IllegalArgumentException("Doesn't need bridge!");
		}
		if (!needsBridge(bridgeType, node2)) {
			throw new IllegalArgumentException("Doesn't need bridge!");
		}
		if (!areNeighbors(node1, node2)) {
			throw new IllegalArgumentException("Crossing bridges!");
		}
		node1.addEdge(bridgeType.getLabel(), node2);
	}

	@Override
	public void removeBridge(GraphBridge bridge) {
		removeGenericBridge(BridgeType.NORMAL, bridge);
	}

	@Override
	public void removeSolutionBridge(GraphBridge bridge) {
		removeGenericBridge(BridgeType.SOLUTION, bridge);
	}

	private void removeGenericBridge(BridgeType bridgeType, GraphBridge bridge) {
		Vertex node1 = getVertexForField(bridge.getField1());
		Vertex node2 = getVertexForField(bridge.getField2());
		if (!areNeighbors(node1, node2)) {
			throw new IllegalArgumentException("Need to be neighbors!");
		}
		List<Object> possibleCandidates = getCandidateBridges(bridgeType, node1, node2);
		if (possibleCandidates.size() == 0) {
			throw new IllegalArgumentException("No " + bridgeType.getLabel() + " to delete found!");
		} else {
			Edge e = (Edge) possibleCandidates.get(0);
			e.remove();
		}
	}

	private List<Object> getCandidateBridges(BridgeType bridgeType, Vertex node1, Vertex node2) {
		return node1.graph().traversal().V(node1).outE(bridgeType.getLabel()).as("edgeToDelete").bothV().is(node2)
				.select("edgeToDelete").toList();
	}

	private static enum BridgeType {
		SOLUTION("solutionBridge"), NORMAL("bridge");
		String label;

		private BridgeType(String label) {
			this.label = label;
		}

		public String getLabel() {
			return this.label;
		}
	}

	private boolean needsBridge(BridgeType bridgeType, Vertex node) {
		return (int) node.property("bridges").value() > node.graph().traversal().V(node).bothE(bridgeType.getLabel())
				.count().toList().get(0);
	}

	private Set<Vertex> getNeighbors(Vertex node) {
		Set<Vertex> vertices = new HashSet<>();
		Graph g = node.graph();

		vertices.addAll(getTopNeighbors(node, g));
		vertices.addAll(getBottomNeighbors(node, g));
		vertices.addAll(getLeftNeighbors(node, g));
		vertices.addAll(getRightNeighbors(node, g));
		Set<Vertex> realVertices = new HashSet<>();
		for (Vertex v : vertices) {
			if (areNeighbors(node, v)) {
				realVertices.add(v);
			}
		}
		return realVertices;
	}

	private Set<Vertex> getRightNeighbors(Vertex node, Graph g) {
		return g.traversal().V(node).repeat(__.out("column")).until(__.values("bridges").is(P.neq(0))).toSet();
	}

	private Set<Vertex> getLeftNeighbors(Vertex node, Graph g) {
		return g.traversal().V(node).repeat(__.in("column")).until(__.values("bridges").is(P.neq(0))).toSet();
	}

	private Set<Vertex> getBottomNeighbors(Vertex node, Graph g) {
		return g.traversal().V(node).repeat(__.out("row")).until(__.values("bridges").is(P.neq(0))).toSet();
	}

	private Set<Vertex> getTopNeighbors(Vertex node, Graph g) {
		return g.traversal().V(node).repeat(__.in("row")).until(__.values("bridges").is(P.neq(0))).toSet();
	}

	private List<GraphBridge> getBridges(Vertex node) {
		List<GraphBridge> bridges = new ArrayList<>();
		Iterator<Edge> edges = node.edges(Direction.BOTH, "bridge");
		while (edges.hasNext()) {
			Edge edge = edges.next();
			GraphBridge bridge = convertBridgeEdgeToGraphBridge(edge);
			if (bridgesToWeight.containsKey(bridge)) {
				bridgesToWeight.put(bridge, bridgesToWeight.get(bridge) + 1);
			} else {
				bridgesToWeight.put(bridge, 1);
			}
			bridges.add(bridge);
		}

		return bridges;
	}

	private GraphBridge convertBridgeEdgeToGraphBridge(Edge edge) {
		GraphField node1 = convertVertexToFieldLight(edge.outVertex());
		GraphField node2 = convertVertexToFieldLight(edge.inVertex());
		GraphBridge bridge = new GraphBridge(node1, node2);
		return bridge;
	}

	private List<GraphBridge> getSolutionBridges(Vertex node) {
		List<GraphBridge> bridges = new ArrayList<>();
		Iterator<Edge> edges = node.edges(Direction.BOTH, "solutionBridge");
		while (edges.hasNext()) {
			Edge edge = edges.next();
			GraphBridge bridge = convertBridgeEdgeToGraphBridge(edge);
			if (solutionBridgesToWeight.containsKey(bridge)) {
				solutionBridgesToWeight.put(bridge, solutionBridgesToWeight.get(bridge) + 1);
			} else {
				solutionBridgesToWeight.put(bridge, 1);
			}
			bridges.add(bridge);
		}

		return bridges;
	}

	private boolean areNeighbors(Vertex node1, Vertex node2) {
		GraphField field1 = convertVertexToFieldLight(node1);
		GraphField field2 = convertVertexToFieldLight(node2);
		Graph g = node1.graph();
		if (!areOrthogonal(field1, field2)) {
			return false;
		} else if (!haveSpaceInBetween(field1, field2)) {
			return false;
		} else if (!haveNoBridgesInBetween(node1, field1, field2, g)) {
			return false;
		} else {
			return true;
		}
	}

	private boolean haveNoBridgesInBetween(Vertex node1, GraphField field1, GraphField field2, Graph g) {
		GraphTraversal<Vertex, Vertex> tr = g.traversal().V(node1);
		Predicate<Traverser<Vertex>> complexFilterColumn = new Predicate<Traverser<Vertex>>() {

			@Override
			public boolean test(Traverser<Vertex> t) {
				List<Vertex> tops = g.traversal().V(t.get()).repeat(__.in("row"))
						.until(__.values("bridges").is(P.neq(0))).toList();
				if (tops.size() > 0) {
					List<Vertex> bottoms = g.traversal().V(t.get()).repeat(__.out("row"))
							.until(__.values("bridges").is(P.neq(0))).toList();
					if (bottoms.size() > 0) {
						Vertex top = tops.get(0);
						Vertex bottom = bottoms.get(0);
						Set<Edge> edges = new HashSet<>();
						Iterator<Edge> topEdges = top.edges(Direction.BOTH, "bridge");
						while (topEdges.hasNext()) {
							edges.add(topEdges.next());
						}

						Iterator<Edge> bottomEdges = bottom.edges(Direction.BOTH, "bridge");
						while (bottomEdges.hasNext()) {
							if (edges.contains(bottomEdges.next())) {
								return false;
							}
						}
					}
				}

				return true;
			}
		};
		Predicate<Traverser<Vertex>> complexFilterRow = new Predicate<Traverser<Vertex>>() {

			@Override
			public boolean test(Traverser<Vertex> t) {
				List<Vertex> tops = g.traversal().V(t.get()).repeat(__.in("column"))
						.until(__.values("bridges").is(P.neq(0))).toList();
				if (tops.size() > 0) {
					List<Vertex> bottoms = g.traversal().V(t.get()).repeat(__.out("column"))
							.until(__.values("bridges").is(P.neq(0))).toList();
					if (bottoms.size() > 0) {
						Vertex top = tops.get(0);
						Vertex bottom = bottoms.get(0);
						Set<Edge> edges = new HashSet<>();
						Iterator<Edge> topEdges = top.edges(Direction.BOTH, "bridge");
						while (topEdges.hasNext()) {
							edges.add(topEdges.next());
						}

						Iterator<Edge> bottomEdges = bottom.edges(Direction.BOTH, "bridge");
						while (bottomEdges.hasNext()) {
							if (edges.contains(bottomEdges.next())) {
								return false;
							}
						}
					}
				}

				return true;
			}
		};
		if (field1.getX() > field2.getX()) {
			tr = tr.repeat(__.in("row")
					.or(__.and(__.values("x").is(P.eq(field2.getX())), __.values("bridges").is(P.neq(0))),
							__.and(__.values("x").is(P.gt(field2.getX())), __.values("bridges").is(0)))
					.filter(complexFilterRow)).until(__.values("x").is(P.eq(field2.getX())));
		}
		if (field1.getX() < field2.getX()) {
			tr = tr.repeat(__.out("row")
					.or(__.and(__.values("x").is(P.eq(field2.getX())), __.values("bridges").is(P.neq(0))),
							__.and(__.values("x").is(P.lt(field2.getX())), __.values("bridges").is(0)))
					.filter(complexFilterRow)).until(__.values("x").is(P.eq(field2.getX())));
		}
		if (field1.getY() > field2.getY()) {
			tr = tr.repeat(__.in("column")
					.or(__.and(__.values("y").is(P.eq(field2.getY())), __.values("bridges").is(P.neq(0))),
							__.and(__.values("y").is(P.gt(field2.getY())), __.values("bridges").is(0)))
					.filter(complexFilterColumn)).until(__.values("y").is(P.eq(field2.getY())));
		}
		if (field1.getY() < field2.getY()) {
			tr = tr.repeat(__.out("column")
					.or(__.and(__.values("y").is(P.eq(field2.getY())), __.values("bridges").is(P.neq(0))),
							__.and(__.values("y").is(P.lt(field2.getY())), __.values("bridges").is(0)))
					.filter(complexFilterColumn)).until(__.values("y").is(P.eq(field2.getY())));
		}
		return tr.toList().size() > 0;
	}

	private boolean haveSpaceInBetween(GraphField field1, GraphField field2) {
		return !(field1.getX() + 1 == field2.getX() || field1.getX() - 1 == field2.getX()
				|| field1.getY() + 1 == field2.getY() || field1.getY() - 1 == field2.getY());
	}

	private boolean areOrthogonal(GraphField field1, GraphField field2) {
		return !(field1.getX() != field2.getX() && field1.getY() != field2.getY());
	}

	@Override
	public void undo() {
		throw new UnsupportedOperationException("undo function is not Implemented in BaseGraphDas");
	}

	@Override
	public boolean canUndo() {
		throw new UnsupportedOperationException("canUndo function is not Implemented in BaseGraphDas");
	}

	@Override
	public void redo() {
		throw new UnsupportedOperationException("redo function is not Implemented in BaseGraphDas");
	}

	@Override
	public boolean canRedo() {
		throw new UnsupportedOperationException("canRedo function is not Implemented in BaseGraphDas");
	}

	@Override
	public void restart() {
		for (GraphBridge bridge : getPlayField().getBridges()) {
			for (int i = 0; i < bridge.getWeighting(); i++) {
				removeBridge(bridge);
			}
		}
	}
}
