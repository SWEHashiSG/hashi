package ch.ntb.swehashisg.hashi.graph;

import ch.ntb.swehashisg.hashi.model.GraphField;

class GraphInitializer {
	public static BaseGraphDas generateExamplePlay(BaseGraphDas g) {
		g.setBridges(new GraphField(0, 1, 1));
		g.setBridges(new GraphField(0, 3, 2));
		g.setBridges(new GraphField(0, 5, 3));
		g.setBridges(new GraphField(0, 7, 1));

		g.setBridges(new GraphField(1, 0, 3));
		g.setBridges(new GraphField(1, 2, 5));
		g.setBridges(new GraphField(1, 4, 3));

		g.setBridges(new GraphField(2, 5, 4));
		g.setBridges(new GraphField(2, 7, 1));

		g.setBridges(new GraphField(3, 2, 4));
		g.setBridges(new GraphField(3, 4, 4));

		g.setBridges(new GraphField(4, 0, 4));
		g.setBridges(new GraphField(4, 3, 5));
		g.setBridges(new GraphField(4, 5, 8));
		g.setBridges(new GraphField(4, 7, 3));

		g.setBridges(new GraphField(5, 6, 1));

		g.setBridges(new GraphField(6, 0, 3));
		g.setBridges(new GraphField(6, 2, 1));
		g.setBridges(new GraphField(6, 7, 1));
		g.setBridges(new GraphField(6, 5, 2));

		g.setBridges(new GraphField(7, 1, 1));
		g.setBridges(new GraphField(7, 3, 4));
		g.setBridges(new GraphField(7, 6, 2));

		return g;
	}
}
