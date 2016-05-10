package ch.ntb.swehashisg.hashi.graph;

public class TestGraph {

	public static void main(String[] args) {

		try {
			GraphDas graphDas = GraphDasFactory.getGraphDas();

			graphDas.getRelevantFields();

			GraphDasFactory.closeGraphDas(graphDas);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
		System.exit(0);
	}

}
