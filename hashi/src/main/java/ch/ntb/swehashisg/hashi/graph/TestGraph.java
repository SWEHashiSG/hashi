package ch.ntb.swehashisg.hashi.graph;

import ch.ntb.swehashisg.hashi.model.GraphField;

public class TestGraph {

	public static void main(String[] args) {

		try {
			GraphDas graphDas = GraphDasFactory.getGraphDas();

			for (GraphField graphField : graphDas.getRelevantFields()) {
				System.out.println("graphField x: " + graphField.getX());
				System.out.println("graphField Y: " + graphField.getY());
				for (GraphField neighbor : graphField.getNeighbors()) {
					System.out.println("Neighbor x: " + neighbor.getX());
					System.out.println("Neighbor Y: " + neighbor.getY());
				}
			}

			GraphDasFactory.closeGraphDas(graphDas);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
		System.exit(0);
	}

}
