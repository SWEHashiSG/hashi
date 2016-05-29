package ch.ntb.swehashisg.hashi.model;

public class GraphBridge {
	private GraphField field1;
	private GraphField field2;
	private BridgeDirection bridgeDirection;
	private boolean highlite;
	private int weighting;

	public GraphBridge(GraphField field1, GraphField field2) {
		this.field1 = field1;
		this.field2 = field2;
		if (field1.getX() == field2.getX()) {
			bridgeDirection = BridgeDirection.Vertical;
		} else if (field1.getY() == field2.getY()) {
			bridgeDirection = BridgeDirection.Horizontal;
		} else {
			throw new IllegalArgumentException("Bridges can only be horizontal or vertical");
		}
		highlite = false;
		weighting = 0;
	}

	public GraphField getField1() {
		return field1;
	}

	public void setField1(GraphField field1) {
		this.field1 = field1;
	}

	public GraphField getField2() {
		return field2;
	}

	public void setField2(GraphField field2) {
		this.field2 = field2;
	}

	public BridgeDirection getBridgeDirection() {
		return bridgeDirection;
	}

	public boolean isHighlited() {
		return highlite;
	}

	public void setHighliter(boolean highlited) {
		highlite = highlited;
	}

	public void setWeighting(Integer weighting) {
		this.weighting = weighting;
	}

	public int getWeighting() {
		return weighting;
	}

	public void incrementWeighting() {
		weighting = ((weighting + 1) % 3);
	}

	public void decrementWeighting() {
		if (weighting == 0) {
			weighting = 2;
		} else {
			weighting = weighting - 1;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraphBridge other = (GraphBridge) obj;
		if (field1.equals(other.field1) && field2.equals(other.field2))
			return true;
		if (field1.equals(other.field2) && field2.equals(other.field1))
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (field1.hashCode() + field2.hashCode());
		return result;
	}

}
