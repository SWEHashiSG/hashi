package ch.ntb.swehashisg.hashi.graph;

import ch.ntb.swehashisg.hashi.model.GraphField;

class GraphInitializer {
	public static BaseGraphDas generateExamplePlay(BaseGraphDas g) {
		g.setBridges(new GraphField(1, 0, 1));
		g.setBridges(new GraphField(3, 0, 2));
		g.setBridges(new GraphField(5, 0, 3));
		g.setBridges(new GraphField(7, 0, 1));

		g.setBridges(new GraphField(0, 1, 3));
		g.setBridges(new GraphField(2, 1, 5));
		g.setBridges(new GraphField(4, 1, 3));

		g.setBridges(new GraphField(5, 2, 4));
		g.setBridges(new GraphField(7, 2, 1));

		g.setBridges(new GraphField(2, 3, 4));
		g.setBridges(new GraphField(4, 3, 4));

		g.setBridges(new GraphField(0, 4, 4));
		g.setBridges(new GraphField(3, 4, 5));
		g.setBridges(new GraphField(5, 4, 8));
		g.setBridges(new GraphField(7, 4, 3));

		g.setBridges(new GraphField(6, 5, 1));

		g.setBridges(new GraphField(0, 6, 3));
		g.setBridges(new GraphField(2, 6, 1));
		g.setBridges(new GraphField(7, 6, 1));
		g.setBridges(new GraphField(5, 6, 2));

		g.setBridges(new GraphField(1, 7, 1));
		g.setBridges(new GraphField(3, 7, 4));
		g.setBridges(new GraphField(6, 7, 2));

		return g;
	}
}
