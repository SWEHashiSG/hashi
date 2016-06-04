package ch.ntb.swehashisg.hashi.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.ntb.swehashisg.hashi.controller.GraphUtil;

/**
 * Represents a Field in the model from the MVC.
 * 
 * @author Martin
 */
public class GraphField {

	/**
	 * X position from this field
	 */
	private int x;

	/**
	 * Y position from this field
	 */
	private int y;

	/**
	 * number of bridges who this fields needs. Must be between 1 and 8
	 */
	private int bridges;

	/**
	 * Set of the direct first horizontal and vertical neighbors.
	 */
	private Set<GraphField> neighbors;

	/**
	 * List of bridges connected to this field.
	 */
	private List<GraphBridge> existingBridges;

	/**
	 * All neighbors from this field. North, south, east and west
	 */
	private GraphField southNeighbor = null;
	private GraphField northNeighbor = null;
	private GraphField eastNeighbor = null;
	private GraphField westNeighbor = null;

	/**
	 * Constructor of GraphField
	 * 
	 * @param x
	 *            position from this field
	 * @param y
	 *            position from this field
	 * @param bridges
	 *            number of needed bridges
	 * @param neighbors
	 *            set of neighbors
	 * @param existingBridges
	 *            list of existing bridges which are connected to this field
	 * @param existingSolutionBridges
	 *            list of solution bridges which are connected to this field
	 */
	public GraphField(int x, int y, int bridges, Set<GraphField> neighbors, List<GraphBridge> existingBridges,
			List<GraphBridge> existingSolutionBridges) {
		this.x = x;
		this.y = y;
		this.bridges = bridges;
		this.neighbors = neighbors;
		this.existingBridges = existingBridges;

		for (GraphField neighbor : this.getNeighbors()) {
			if (GraphUtil.isEast(this, neighbor)) {
				eastNeighbor = neighbor;
			}
			if (GraphUtil.isWest(this, neighbor)) {
				westNeighbor = neighbor;
			}
			if (GraphUtil.isSouth(this, neighbor)) {
				southNeighbor = neighbor;
			}
			if (GraphUtil.isNorth(this, neighbor)) {
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

	/**
	 * fields are equals if X and Y position are the same
	 */
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
