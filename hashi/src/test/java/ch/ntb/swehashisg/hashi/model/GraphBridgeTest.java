package ch.ntb.swehashisg.hashi.model;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

public class GraphBridgeTest {

	GraphField field1, field2, field3, field4, field5;
	GraphBridge graphBridge1, graphBridge2;
	HashSet<GraphField> field2neighbor;

	@Before
	public void preparations() {
		field3 = new GraphField(5, 5, 1);
		HashSet<GraphField> neighbor = new HashSet<GraphField>();
		neighbor.add(field3);
		field4 = new GraphField(4, 2, 2, neighbor, null, null);
		field5 = new GraphField(6, 2, 2);

		field1 = new GraphField(1, 1, 2);
		field2neighbor = new HashSet<GraphField>();
		field2neighbor.add(field1);
		
		setToHorizontalNeighbors(2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGraphBridge() {
		graphBridge2 = new GraphBridge(field3, field4);
	}

	private void setToVerticalNeighbors(int field2bridges) {
		field2 = new GraphField(1, 2, field2bridges, field2neighbor, null, null);
		graphBridge1 = new GraphBridge(field1, field2);
	}

	private void setToHorizontalNeighbors(int field2bridges) {
		field2 = new GraphField(2, 1, field2bridges, field2neighbor, null, null);
		graphBridge1 = new GraphBridge(field1, field2);
	}

	@Test
	public void testHashCode() {
		assertTrue("HashCode calculated wrong", graphBridge1.hashCode() == 31 + field1.hashCode() + field2.hashCode());
	}

	@Test
	public void testGetField1() {
		assertTrue("field1 should be field1", field1.equals(graphBridge1.getField1()));
	}

	@Test
	public void testSetField1() {
		graphBridge1.setField1(field3);
		assertTrue("field1 should be field3 now", field3.equals(graphBridge1.getField1()));
		graphBridge1.setField1(field1);
	}

	@Test
	public void testGetField2() {
		assertTrue("Field2 should be field2", field2.equals(graphBridge1.getField2()));
	}

	@Test
	public void testSetField2() {
		graphBridge1.setField2(field3);
		assertTrue("field2 should be field3 now", field3.equals(graphBridge1.getField2()));
		graphBridge1.setField2(field2);
	}

	@Test
	public void testGetBridgeDirection() {
		setToVerticalNeighbors(4);
		assertTrue("Bridge should be vertical", graphBridge1.getBridgeDirection().equals(BridgeDirection.Vertical));
		setToHorizontalNeighbors(4);
		assertTrue("Bridge should be horizontal", graphBridge1.getBridgeDirection().equals(BridgeDirection.Horizontal));
		setToVerticalNeighbors(4);
	}

	@Test
	public void testSetWeighting() {
		assertTrue("Weighting should be 0", graphBridge1.getWeighting() == 0);
		graphBridge1.setWeighting(1);
		assertTrue("Weighting should be 1", graphBridge1.getWeighting() == 1);
		graphBridge1.setWeighting(2);
		assertTrue("Weighting should be 2", graphBridge1.getWeighting() == 2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetWeighting2() {
		graphBridge1.setWeighting(3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetWeightingMinus1() {
		graphBridge1.setWeighting(-1);
	}

	@Test
	public void testEqualsObject() {
		graphBridge2 = new GraphBridge(field5, field4);
		boolean workaroundForCoverage = graphBridge1.equals(graphBridge2);
		assertTrue("Should not be the same bridge", !workaroundForCoverage);
		workaroundForCoverage = graphBridge1.equals(graphBridge1);
		assertTrue("Should be the same bridge", workaroundForCoverage);
		graphBridge2 = new GraphBridge(field1, field2);
		assertTrue("SHould be the same bridge", graphBridge1.equals(graphBridge2));
		graphBridge2 = new GraphBridge(field2, field1);
		assertTrue("SHould be the same bridge", graphBridge1.equals(graphBridge2));
		assertTrue("Should be false and throw not an Exception",!graphBridge1.equals(null));
		assertTrue("Should be false and throw not an Exception", !graphBridge1.equals(new Object()));
	}
}
