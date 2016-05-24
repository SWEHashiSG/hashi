package ch.ntb.swehashisg.hashi.controller;

import ch.ntb.swehashisg.hashi.model.GraphField;

public class GraphUtil {
	public static boolean isNorthBridge(GraphField field, GraphField neighbor) {
		if (isVerticalNeighbor(field, neighbor)) {
			if (field.getY() < neighbor.getY()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSouthBridge(GraphField field, GraphField neighbor) {
		if (isVerticalNeighbor(field, neighbor)) {
			if (field.getY() > neighbor.getY()) {
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
