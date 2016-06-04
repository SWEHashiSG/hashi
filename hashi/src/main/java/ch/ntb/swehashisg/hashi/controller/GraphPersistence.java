package ch.ntb.swehashisg.hashi.controller;

import ch.ntb.swehashisg.hashi.graph.GraphFormat;

public class GraphPersistence {

	private String path;
	private GraphFormat graphFormat;

	public GraphPersistence(String path, GraphFormat graphFormat) {
		this.path = path;
		this.graphFormat = graphFormat;
	}

	public String getPath() {
		return path;
	}

	public GraphFormat getGraphFormat() {
		return graphFormat;
	}

}
