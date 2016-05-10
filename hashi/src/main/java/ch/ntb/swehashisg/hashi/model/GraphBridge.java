package ch.ntb.swehashisg.hashi.model;

public class GraphBridge {
	private GraphField field1;
	private GraphField field2;

	public GraphBridge(GraphField field1, GraphField field2) {
		this.field1 = field1;
		this.field2 = field2;
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
}
