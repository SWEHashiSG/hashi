package ch.ntb.swehashisg.hashi.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import ch.ntb.swehashisg.hashi.model.BridgeDirection;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;

public class GraphBridgeTest {

	GraphField field1, field2, field3, field4;
	GraphBridge graphBridge1, graphBridge2;
	HashSet<GraphField> field2neighbor;

	@Before
	public void preparations() {

		field3 = new GraphField(5, 5, 1); // Dummy
		HashSet<GraphField> neighbor = new HashSet<GraphField>();
		neighbor.add(field3);
		field4 = new GraphField(4, 2, 2, neighbor, null);

		field1 = new GraphField(1, 1, 2);
		field2neighbor = new HashSet<GraphField>();
		field2neighbor.add(field1);

		setToVerticalNeighbors(4);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGraphBridge() {
		graphBridge2 = new GraphBridge(field3, field4);
	}

	private void setToVerticalNeighbors(int field2bridges) {
		field2 = new GraphField(1, 2, field2bridges, field2neighbor, null);
		graphBridge1 = new GraphBridge(field1, field2);
	}

	private void setToHorizontalNeighbors(int field2bridges) {
		field2 = new GraphField(2, 1, field2bridges, field2neighbor, null);
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
		assertTrue("Bridge should be vertical", graphBridge1.getBridgeDirection().equals(BridgeDirection.Vertical));
		setToHorizontalNeighbors(4);
		assertTrue("Bridge should be horizontal", graphBridge1.getBridgeDirection().equals(BridgeDirection.Horizontal));
		setToVerticalNeighbors(4);
	}

	@Test
	public void testIsHighlited() {
		assertTrue("Should not be highlighted", graphBridge1.isHighlited() == false);
		graphBridge1.setHighliter(true);
		assertTrue("Should be hihglighted now", graphBridge1.isHighlited() == true);
		graphBridge1.setHighliter(false);
		assertTrue("Shouldn't be highlighted anymore", graphBridge1.isHighlited() == false);
	}

	@Test
	public void testSetHighliter() {
		// already tested in testIsHighlighted()
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
	public void testSetWeighting3() {
		setToHorizontalNeighbors(1);
		graphBridge1.setWeighting(2);
	}

	@Test
	public void testGetWeighting() {
		// already tested in testSetWeighting()
	}

	@Test
	public void testIncrementWeighting() {
		setToHorizontalNeighbors(2);
		graphBridge1.setWeighting(0);
		assertTrue("Weighting should be 0", graphBridge1.getWeighting() == 0);
		graphBridge1.incrementWeighting();
		assertTrue("Weighting should be 1", graphBridge1.getWeighting() == 1);
		graphBridge1.incrementWeighting();
		assertTrue("Weighting should be 2", graphBridge1.getWeighting() == 2);

		setToHorizontalNeighbors(1);
		graphBridge1.setWeighting(0);
		graphBridge1.incrementWeighting();
		assertTrue("Weighting should be 1", graphBridge1.getWeighting() == 1);

		setToHorizontalNeighbors(4);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testIncrementWeighting2() {
		setToHorizontalNeighbors(2);
		graphBridge1.setWeighting(2);
		graphBridge1.incrementWeighting();
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testIncrementWeighting3() {
		setToHorizontalNeighbors(1);
		graphBridge1.setWeighting(1);
		graphBridge1.incrementWeighting();
	}

	@Test
	public void testDecrementWeighting() {
		setToHorizontalNeighbors(2);
		graphBridge1.setWeighting(2);
		assertTrue("Weighting should be 2", graphBridge1.getWeighting() == 2);
		graphBridge1.decrementWeighting();
		assertTrue("Weighting should be 1", graphBridge1.getWeighting() == 1);
		graphBridge1.decrementWeighting();
		assertTrue("Weighting should be 0", graphBridge1.getWeighting() == 0);

		setToHorizontalNeighbors(1);

		graphBridge1.setWeighting(1);
		graphBridge1.decrementWeighting();
		assertTrue("Weighting should be 0", graphBridge1.getWeighting() == 0);

		setToHorizontalNeighbors(4);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testDecrementWeighting2() {
		graphBridge1.setWeighting(0);
		graphBridge1.decrementWeighting();
	}

	@Test
	public void testEqualsObject() {
		graphBridge2 = new GraphBridge(field2, field4);
		boolean workaroundForCoverage = graphBridge1.equals(graphBridge2);
		assertTrue("Should not be the same bridge", !workaroundForCoverage);
		workaroundForCoverage = graphBridge1.equals(graphBridge1);
		assertTrue("Should be the same bridge", workaroundForCoverage);
	}
	
	@Test
	public void testEqualsObject2() {
		assertTrue("Should return a 'null'", graphBridge1.equals(null));
	}

}
