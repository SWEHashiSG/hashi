package ch.ntb.swehashisg.hashi.controller;

import ch.ntb.swehashisg.hashi.model.BridgeDirection;
import ch.ntb.swehashisg.hashi.model.GraphField;

/**
 * Utility class which collects some useful functions for the controller classes
 * 
 * @author Philippe
 *
 */
public class GraphUtil {

	/**
	 * @param field1
	 *            from Bridge
	 * @param field2
	 *            from Bridge
	 * @return Direction of Bridge
	 */
	public static BridgeDirection getDirectionOfNeighbors(GraphField field1, GraphField field2) {
		if (isVerticalNeighbor(field1, field2)) {
			return BridgeDirection.Vertical;
		} else if (isHorizontalNeighbor(field1, field2)) {
			return BridgeDirection.Horizontal;
		} else {
			throw new IllegalArgumentException("Bridges can only be horizontal or vertical");
		}
	}

	/**
	 * @param field
	 * @param neighbor
	 * @return true if neighbor is north to the field
	 */
	public static boolean isNorth(GraphField field, GraphField neighbor) {
		if (isVerticalNeighbor(field, neighbor)) {
			if (field.getY() > neighbor.getY()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param field
	 * @param neighbor
	 * @return true if neighbor is south to the field
	 */
	public static boolean isSouth(GraphField field, GraphField neighbor) {
		if (isVerticalNeighbor(field, neighbor)) {
			if (field.getY() < neighbor.getY()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param field
	 * @param neighbor
	 * @return true if neighbor is east to the field
	 */
	public static boolean isEast(GraphField field, GraphField neighbor) {
		if (isHorizontalNeighbor(field, neighbor)) {
			if (field.getX() < neighbor.getX()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param field
	 * @param neighbor
	 * @return true if neighbor is west to field
	 */
	public static boolean isWest(GraphField field, GraphField neighbor) {
		if (isHorizontalNeighbor(field, neighbor)) {
			if (field.getX() > neighbor.getX()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * internal used Class
	 * 
	 * @param field
	 * @param neighbor
	 * @return true if field and neighbor have the same X position
	 */
	private static boolean isVerticalNeighbor(GraphField field, GraphField neighbor) {
		return field.getX() == neighbor.getX();
	}

	/**
	 * 
	 * @param field
	 * @param neighbor
	 * @return true if field and neighbor have the same Y position
	 */
	private static boolean isHorizontalNeighbor(GraphField field, GraphField neighbor) {
		return field.getY() == neighbor.getY();
	}
}
