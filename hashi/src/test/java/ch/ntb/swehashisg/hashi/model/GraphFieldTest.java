package ch.ntb.swehashisg.hashi.model;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

import ch.ntb.swehashisg.hashi.model.GraphField;

public class GraphFieldTest {

	GraphField main, northN, southN, westN, eastN;
	HashSet<GraphField> neighbor;

	public GraphFieldTest() {
		main = new GraphField(5, 5, 2);
		neighbor = new HashSet<GraphField>();
		neighbor.add(main);

		northN = new GraphField(5, 1, 2, neighbor, null, null);
		southN = new GraphField(5, 10, 2, neighbor, null, null);
		westN = new GraphField(1, 5, 2, neighbor, null, null);
		eastN = new GraphField(10, 5, 2, neighbor, null, null);
	}

	@Test
	public void testHashCode() {
		assertTrue("HashCode not right", main.hashCode() == 31 * (31 + main.getX()) + main.getY());
	}

	@Test
	public void testHasNorthNeighbor() {
		assertTrue("Should have North-Neighbor", southN.hasNorthNeighbor());
	}

	@Test
	public void testHasSouthNeighbor() {
		assertTrue("Should have South-Neighbro", northN.hasSouthNeighbor());
	}

	@Test
	public void testHasEastNeighbor() {
		assertTrue("Should have East-Neighbor", westN.hasEastNeighbor());
	}

	@Test
	public void testHasWestNeighbor() {
		assertTrue("Should have West-Neighbor", eastN.hasWestNeighbor());
	}

	@Test
	public void testGetSouthNeighbor() {
		assertTrue("Should be South-Neighbor", northN.getSouthNeighbor().equals(main));
	}

	@Test
	public void testGetNorthNeighbor() {
		assertTrue("Should be North-Neighbor", southN.getNorthNeighbor().equals(main));
	}

	@Test
	public void testGetEastNeighbor() {
		assertTrue("Should be East-Neighbor", westN.getEastNeighbor().equals(main));
	}

	@Test
	public void testGetWestNeighbor() {
		assertTrue("Should be West-Neighbor", eastN.getWestNeighbor().equals(main));
	}

	@Test
	public void testGetX() {
		assertTrue("Should be 5", main.getX() == 5);
	}

	@Test
	public void testGetY() {
		assertTrue("Should be 5", main.getY() == 5);
	}

	@Test
	public void testGetBridges() {
		assertTrue("Should be 2", main.getBridges() == 2);
	}

	@Test
	public void testGetNeighbors() {
		fail("Not yet impemented");
	}

	@Test
	public void testGetExistingBridges() {
		fail("Not yet implemented");
	}

}
