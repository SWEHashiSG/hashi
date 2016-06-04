package ch.ntb.swehashisg.hashi.model;

import ch.ntb.swehashisg.hashi.controller.GraphUtil;

/**
 * Represents a Bridge from one to another Field.
 * 
 * @author Martin
 */
public class GraphBridge {

	/**
	 * One field were the bridge starts/ends.
	 */
	private GraphField field1;

	/**
	 * The other field were the bridge starts/ends.
	 */
	private GraphField field2;

	/**
	 * Direction of the bridge. Horizontal or vertical
	 */
	private BridgeDirection bridgeDirection;

	/**
	 * How many bridges are drawn from field1 to field 2. weighting must be
	 * between 0 and 2.
	 */
	private int weighting;

	/**
	 * Constructor of GraphBridge. Checks the direction of the bridge and set
	 * the weighting to zero.
	 * 
	 * @param field1
	 *            GraphField on one side of the bridge.
	 * @param field2
	 *            GraphField on the other side of the bridge.
	 */
	public GraphBridge(GraphField field1, GraphField field2) {
		this.field1 = field1;
		this.field2 = field2;
		bridgeDirection = GraphUtil.getDirectionOfNeighbors(field1, field2);
		if (field1.getX() == field2.getX()) {
			bridgeDirection = BridgeDirection.Vertical;
		} else if (field1.getY() == field2.getY()) {
			bridgeDirection = BridgeDirection.Horizontal;
		} else {
			throw new IllegalArgumentException("Bridges can only be horizontal or vertical");
		}
		setWeighting(0);
	}

	public GraphField getField1() {
		return field1;
	}

	public void setField1(GraphField field1) {
		this.field1 = field1;
	}

	public GraphField getField2() {
		return field2;
	}

	public void setField2(GraphField field2) {
		this.field2 = field2;
	}

	public BridgeDirection getBridgeDirection() {
		return bridgeDirection;
	}

	/**
	 * @param weighting
	 *            must be between 0 and 2. Otherwise throws
	 *            IllegalArgumentException
	 */
	public void setWeighting(Integer weighting) {
		if (weighting > 2 || weighting < 0) {
			throw new IllegalArgumentException("weighting must be between 0 and 2 but was: " + weighting);
		}
		this.weighting = weighting;
	}

	public int getWeighting() {
		return weighting;
	}

	/**
	 * Override the Equals Function. A bridge is equals if both fields are
	 * equals. The place doesn't matter.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraphBridge other = (GraphBridge) obj;
		if (field1.equals(other.field1) && field2.equals(other.field2))
			return true;
		if (field1.equals(other.field2) && field2.equals(other.field1))
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (field1.hashCode() + field2.hashCode());
		return result;
	}
}
