package ch.ntb.swehashisg.hashi.graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;

public class TestGraph {

	private static final Logger logger = LoggerFactory.getLogger(TestGraph.class);

	public static void main(String[] args) {

		try {
			GraphDas graphDas = GraphDasFactory.getGraphDas();

			for (GraphField graphField : graphDas.getPlayField().getFields()) {
				logger.debug("graphField x: " + graphField.getX());
				logger.debug("graphField Y: " + graphField.getY());
				for (GraphField neighbor : graphField.getNeighbors()) {
					logger.debug("Neighbor x: " + neighbor.getX());
					logger.debug("Neighbor Y: " + neighbor.getY());
				}
			}

			GraphField field1 = new GraphField(1, 2);
			GraphField field2 = new GraphField(1, 4);

			GraphBridge graphBridge1 = new GraphBridge(field1, field2);

			GraphField field3 = new GraphField(0, 3);
			GraphField field4 = new GraphField(4, 3);

			GraphBridge graphBridge2 = new GraphBridge(field3, field4);

			graphDas.addBridge(graphBridge1);
			logger.debug("Oh Yes!");

			long start = System.currentTimeMillis();

			for (int i = 0; i < 100; i++) {
				graphDas.getPlayField();
			}

			logger.debug("Duration: " + (System.currentTimeMillis() - start));

			try {
				graphDas.addBridge(graphBridge2);
				logger.debug("Oh No!");
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
