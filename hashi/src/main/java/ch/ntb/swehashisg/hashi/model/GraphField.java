package ch.ntb.swehashisg.hashi.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphField {
	private int x;
	private int y;
	private int bridges;
	private Set<GraphField> neighbors;
	private List<GraphBridge> existingBridges;

	public GraphField(int x, int y, int bridges, Set<GraphField> neighbors, List<GraphBridge> existingBridges) {
		this.x = x;
		this.y = y;
		this.bridges = bridges;
		this.neighbors = neighbors;
		this.existingBridges = existingBridges;
	}

	public GraphField(int x, int y, int bridges) {
		this(x, y, bridges, new HashSet<>(), new ArrayList<>());
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

	public Set<GraphField> getNeighbors() {
		return neighbors;
	}

	public List<GraphBridge> getExistingBridges() {
		return existingBridges;
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
		GraphField other = (GraphField) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
