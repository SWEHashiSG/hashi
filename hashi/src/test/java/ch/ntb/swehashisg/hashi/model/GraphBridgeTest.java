package ch.ntb.swehashisg.hashi.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;

import org.junit.Test;

import ch.ntb.swehashisg.hashi.model.BridgeDirection;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;

public class GraphBridgeTest {

	GraphField field1, field2, field3;
	GraphBridge graphBridge;

	public GraphBridgeTest() {

		field3 = new GraphField(2, 5, 1); // Dummy
		setToVerticalNeighbors(4);

	}

	private void setToVerticalNeighbors(int field2bridges) {
		field1 = new GraphField(1, 1, 2);
		HashSet<GraphField> field2neighbor = new HashSet<GraphField>();
		field2neighbor.add(field1);
		field2 = new GraphField(1, 2, field2bridges, field2neighbor, null, null);

		graphBridge = new GraphBridge(field1, field2);
	}

	private void setToHorizontalNeighbors(int field2bridges) {
		field1 = new GraphField(1, 1, 2);
		HashSet<GraphField> field2neighbor = new HashSet<GraphField>();
		field2neighbor.add(field1);
		field2 = new GraphField(2, 1, field2bridges, field2neighbor, null, null);

		graphBridge = new GraphBridge(field1, field2);
	}

	@Test
	public void testHashCode() {
		assertTrue("HashCode calculated wrong", graphBridge.hashCode() == 31 + field1.hashCode() + field2.hashCode());
	}

	@Test
	public void testGetField1() {
		assertTrue("field1 should be field1", field1.equals(graphBridge.getField1()));
	}

	@Test
	public void testSetField1() {
		graphBridge.setField1(field3);
		assertTrue("field1 should be field3 now", field3.equals(graphBridge.getField1()));
		graphBridge.setField1(field1);
	}

	@Test
	public void testGetField2() {
		assertTrue("Field2 should be field2", field2.equals(graphBridge.getField2()));
	}

	@Test
	public void testSetField2() {
		graphBridge.setField2(field3);
		assertTrue("field2 should be field3 now", field3.equals(graphBridge.getField2()));
		graphBridge.setField2(field2);
	}

	@Test
	public void testGetBridgeDirection() {
		assertTrue("Bridge should be vertical", graphBridge.getBridgeDirection().equals(BridgeDirection.Vertical));
		setToHorizontalNeighbors(4);
		assertTrue("Bridge should be horizontal", graphBridge.getBridgeDirection().equals(BridgeDirection.Horizontal));
		setToVerticalNeighbors(4);
	}

	@Test
	public void testIsHighlited() {
		assertTrue("Should not be highlighted", graphBridge.isHighlited() == false);
		graphBridge.setHighliter(true);
		assertTrue("Should be hihglighted now", graphBridge.isHighlited() == true);
		graphBridge.setHighliter(false);
		assertTrue("Shouldn't be highlighted anymore", graphBridge.isHighlited() == false);
	}

	@Test
	public void testSetHighliter() {
		// already tested in testIsHighlighted()
	}

	@Test
	public void testSetWeighting() {
		assertTrue("Weighting should be 0", graphBridge.getWeighting() == 0);
		graphBridge.setWeighting(1);
		assertTrue("Weighting should be 1", graphBridge.getWeighting() == 1);
		graphBridge.setWeighting(2);
		assertTrue("Weighting should be 2", graphBridge.getWeighting() == 2);

		try {
			graphBridge.setWeighting(3);
			fail("Weighting 3 should never be allowed");
		} catch (Exception e) {
		}

		setToHorizontalNeighbors(1);

		try {
			graphBridge.setWeighting(2);
			fail("Weighting 2 should not be possible with field that allows only 1 bridge");
		} catch (Exception e) {
		}

	}

	@Test
	public void testGetWeighting() {
		// already tested in testSetWeighting()
	}

	@Test
	public void testIncrementWeighting() {
		setToHorizontalNeighbors(2);
		graphBridge.setWeighting(0);
		assertTrue("Weighting should be 0", graphBridge.getWeighting() == 0);
		graphBridge.incrementWeighting();
		assertTrue("Weighting should be 1", graphBridge.getWeighting() == 1);
		graphBridge.incrementWeighting();
		assertTrue("Weighting should be 2", graphBridge.getWeighting() == 2);

		try {
			graphBridge.incrementWeighting();
			fail("Incrementing shouldn't be possible if already 2");
		} catch (Exception e) {
		}

		setToHorizontalNeighbors(1);
		graphBridge.setWeighting(0);
		graphBridge.incrementWeighting();
		assertTrue("Weighting should be 1", graphBridge.getWeighting() == 1);

		try {
			graphBridge.incrementWeighting();
			fail("Incrementing shouldn't be possible if only 1 is possible");
		} catch (Exception e) {
		}

		setToHorizontalNeighbors(4);
	}

	@Test
	public void testDecrementWeighting() {
		setToHorizontalNeighbors(2);
		graphBridge.setWeighting(2);
		assertTrue("Weighting should be 2", graphBridge.getWeighting() == 2);
		graphBridge.decrementWeighting();
		assertTrue("Weighting should be 1", graphBridge.getWeighting() == 1);
		graphBridge.decrementWeighting();
		assertTrue("Weighting should be 0", graphBridge.getWeighting() == 0);

		try {
			graphBridge.decrementWeighting();
			fail("Decrementing shouldn't be possible if already 0");
		} catch (Exception e) {
		}

		setToHorizontalNeighbors(1);

		graphBridge.setWeighting(1);
		graphBridge.decrementWeighting();
		assertTrue("Weighting should be 0", graphBridge.getWeighting() == 0);

		setToHorizontalNeighbors(4);
	}

	@Test
	public void testEqualsObject() {
		fail("Not yet implemented");
	}

}
