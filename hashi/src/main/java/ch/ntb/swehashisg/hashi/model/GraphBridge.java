package ch.ntb.swehashisg.hashi.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class GraphBridge {
	private GraphField field1;
	private GraphField field2;
	private BridgeDirection bridgeDirection;
	private BooleanProperty highlite;
	private IntegerProperty weighting;

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
		highlite = new SimpleBooleanProperty(false);
		weighting = new SimpleIntegerProperty(0);
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
		return highlite.get();
	}

	public void setHighliter(boolean highlited) {
		highlite.set(highlited);
	}

	public BooleanProperty getHighlitedProperty() {
		return highlite;
	}

	public int getWeighting() {
		return weighting.get();
	}

	public void incrementWeighting() {
		weighting.set((weighting.get() + 1) % 3);
	}

	public void decrementWeighting() {
		if (weighting.get() == 0) {
			weighting.set(2);
		} else {
			weighting.set(weighting.get() - 1);
		}
	}

	public IntegerProperty getWeightingProperty() {
		return weighting;
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
}
