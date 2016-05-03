package ch.ntb.swehashisg.hashi.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.apache.tinkerpop.gremlin.process.traversal.Operator;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.apache.tinkerpop.gremlin.util.function.ArrayListSupplier;

public class TestGraph {

	public static void main(String[] args) {

		try {
			TinkerGraph tg = TinkerGraph.open();
			tg.createIndex("x", Vertex.class);
			tg.createIndex("y", Vertex.class);
			Graph g = tg;
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
			setBridges(new Field(0, 1, 1, null), g);
			setBridges(new Field(0, 3, 2, null), g);
			setBridges(new Field(0, 5, 3, null), g);
			setBridges(new Field(0, 7, 1, null), g);

			setBridges(new Field(1, 0, 3, null), g);
			setBridges(new Field(1, 2, 5, null), g);
			setBridges(new Field(1, 4, 3, null), g);

			setBridges(new Field(2, 5, 4, null), g);
			setBridges(new Field(2, 7, 1, null), g);

			setBridges(new Field(3, 2, 4, null), g);
			setBridges(new Field(3, 4, 4, null), g);

			setBridges(new Field(4, 0, 4, null), g);
			setBridges(new Field(4, 3, 5, null), g);
			setBridges(new Field(4, 5, 8, null), g);
			setBridges(new Field(4, 7, 3, null), g);

			setBridges(new Field(5, 6, 1, null), g);

			setBridges(new Field(6, 0, 3, null), g);
			setBridges(new Field(6, 2, 1, null), g);
			setBridges(new Field(6, 7, 1, null), g);
			setBridges(new Field(6, 5, 2, null), g);

			setBridges(new Field(7, 1, 1, null), g);
			setBridges(new Field(7, 3, 4, null), g);
			setBridges(new Field(7, 6, 2, null), g);

			g.io(IoCore.graphson()).writeGraph("test.json");
			g.io(IoCore.graphml()).writeGraph("test.xml");

			getRelevantFields(g);

			g.close();
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			System.exit(-1);
		}
		System.exit(0);
	}

	private static void setBridges(Field field, Graph g) {
		Vertex node = getVertexForField(field, g);
		node.property("bridges", field.getBridges());
	}

	public static Set<Field> getRelevantFields(Graph g) {
		Vertex root = g.traversal().V().has("x", 0).has("y", 0).toList().get(0);

		Set<Vertex> vertices = new HashSet<>();
		g.traversal().V(root).dedup().repeat(__.out("row", "column").sideEffect(t -> {
			if ((int) t.get().value("bridges") > 0)
				vertices.add(t.get());
		})).until(__.out("row", "column").count().is(0)).iterate();
		return convertVerticesToFields(vertices);
	}

	private static Set<Field> convertVerticesToFields(Set<Vertex> vertices) {
		Set<Field> fields = new HashSet<>();
		for (Vertex v : vertices) {
			fields.add(convertVertexToField(v));
		}
		return fields;
	}

	private static Vertex getVertexForField(Field field, Graph g) {
		return g.traversal().V().has("x", field.getX()).has("y", field.getY()).toList().get(0);
	}

	private static Field convertVertexToField(Vertex v) {
		int x = (int) v.property("x").value();
		int y = (int) v.property("y").value();
		int bridges = (int) v.property("bridges").value();
		Set<Vertex> neighbors = getNeighbors(v);
		Set<Field> neighborFields = convertVerticesToFields(neighbors);
		return new Field(x, y, bridges, neighborFields);
	}

	private static class Field {
		private int x;
		private int y;
		private int bridges;
		private Set<Field> neighbors;

		public Field(int x, int y, int bridges, Set<Field> neighbors) {
			this.x = x;
			this.y = y;
			this.bridges = bridges;
			this.neighbors = neighbors;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getBridges() {
			return bridges;
		}

		public Set<Field> getNeighbors() {
			return neighbors;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Field other = (Field) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}
	}

	private static class Bridge {
		private Field field1;
		private Field field2;

		public Bridge(Field field1, Field field2) {
			super();
			this.field1 = field1;
			this.field2 = field2;
		}

		public Field getField1() {
			return field1;
		}

		public void setField1(Field field1) {
			this.field1 = field1;
		}

		public Field getField2() {
			return field2;
		}

		public void setField2(Field field2) {
			this.field2 = field2;
		}

	}

	public static void addBridge(Bridge bridge, Graph g) {
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

	public static void removeBridge(Bridge bridge, Graph g) {
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
