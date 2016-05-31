package ch.ntb.swehashisg.hashi.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.ntb.swehashisg.hashi.controller.GraphUtil;

public class GraphField {
	private int x;
	private int y;
	private int bridges;
	private Set<GraphField> neighbors;
	private List<GraphBridge> existingBridges;
	private List<GraphBridge> existingSolutionBridges;

	public List<GraphBridge> getExistingSolutionBridges() {
		return existingSolutionBridges;
	}

	private GraphField southNeighbor = null;
	private GraphField northNeighbor = null;
	private GraphField eastNeighbor = null;
	private GraphField westNeighbor = null;

	public GraphField(int x, int y, int bridges, Set<GraphField> neighbors, List<GraphBridge> existingBridges,
			List<GraphBridge> existingSolutionBridges) {
		this.x = x;
		this.y = y;
		this.bridges = bridges;
		this.neighbors = neighbors;
		this.existingBridges = existingBridges;
		this.existingSolutionBridges = existingSolutionBridges;

		for (GraphField neighbor : this.getNeighbors()) {
			if (GraphUtil.isEastBridge(this, neighbor)) {
				eastNeighbor = neighbor;
			}
			if (GraphUtil.isWestBridge(this, neighbor)) {
				westNeighbor = neighbor;
			}
			if (GraphUtil.isSouthBridge(this, neighbor)) {
				southNeighbor = neighbor;
			}
			if (GraphUtil.isNorthBridge(this, neighbor)) {
				northNeighbor = neighbor;
			}
		}

	}

	public GraphField(int x, int y, int bridges) {
		this(x, y, bridges, new HashSet<>(), new ArrayList<>(), new ArrayList<>());
	}

	public GraphField(int x, int y) {
		this(x, y, 0, new HashSet<>(), new ArrayList<>(), new ArrayList<>());
	}

	public boolean hasNorthNeighbor() {
		return this.northNeighbor != null;
	}

	public boolean hasSouthNeighbor() {
		return this.southNeighbor != null;
	}

	public boolean hasEastNeighbor() {
		return this.eastNeighbor != null;
	}

	public boolean hasWestNeighbor() {
		return this.westNeighbor != null;
	}

	public GraphField getSouthNeighbor() {
		return southNeighbor;
	}

	public GraphField getNorthNeighbor() {
		return northNeighbor;
	}

	public GraphField getEastNeighbor() {
		return eastNeighbor;
	}

	public GraphField getWestNeighbor() {
		return westNeighbor;
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
