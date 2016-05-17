package ch.ntb.swehashisg.hashi.graph;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
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

			GraphField field1 = new GraphField(1, 2);
			GraphField field2 = new GraphField(1, 4);

			GraphBridge graphBridge1 = new GraphBridge(field1, field2);

			GraphField field3 = new GraphField(0, 3);
			GraphField field4 = new GraphField(4, 3);

			GraphBridge graphBridge2 = new GraphBridge(field3, field4);

			graphDas.addBridge(graphBridge1);
			System.out.println("Oh Yes!");

			graphDas.getRelevantFields();

			try {
				graphDas.addBridge(graphBridge2);
				System.out.println("Oh No!");
			} catch (Exception ex) {
				// Expected exception
			}

			GraphDasFactory.closeGraphDas(graphDas);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
		System.exit(0);
	}

}
