package ch.ntb.swehashisg.hashi.controller;

import ch.ntb.swehashisg.hashi.model.BridgeDirection;
import ch.ntb.swehashisg.hashi.model.GraphField;

public class GraphUtil {

	public static BridgeDirection getDirectionOfNeighbors(GraphField field1, GraphField field2) {
		if (field1.getX() == field2.getX()) {
			return BridgeDirection.Vertical;
		} else if (field1.getY() == field2.getY()) {
			return BridgeDirection.Horizontal;
		} else {
			throw new IllegalArgumentException("Bridges can only be horizontal or vertical");
		}
	}

	public static boolean isNorthBridge(GraphField field, GraphField neighbor) {
		if (isVerticalNeighbor(field, neighbor)) {
			if (field.getY() > neighbor.getY()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSouthBridge(GraphField field, GraphField neighbor) {
		if (isVerticalNeighbor(field, neighbor)) {
			if (field.getY() < neighbor.getY()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEastBridge(GraphField field, GraphField neighbor) {
		if (isHorizontalNeighbor(field, neighbor)) {
			if (field.getX() < neighbor.getX()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isWestBridge(GraphField field, GraphField neighbor) {
		if (isHorizontalNeighbor(field, neighbor)) {
			if (field.getX() > neighbor.getX()) {
				return true;
			}
		}
		return false;
	}

	private static boolean isVerticalNeighbor(GraphField field, GraphField neighbor) {
		return field.getX() == neighbor.getX();
	}

	private static boolean isHorizontalNeighbor(GraphField field, GraphField neighbor) {
		return field.getY() == neighbor.getY();
	}
}
