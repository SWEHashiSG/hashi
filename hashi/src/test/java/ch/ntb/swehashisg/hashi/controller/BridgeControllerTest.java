package ch.ntb.swehashisg.hashi.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;

public class BridgeControllerTest {
	
	GameFieldController gameFieldController;
	BridgeController bridgeController;
	GraphBridge graphBridge;
	GraphField field1, field2;
	
	@Before
	public void preparations() {
		field1 = new GraphField(1, 2, 2);
		field2 = new GraphField(2, 2, 2);
		graphBridge = new GraphBridge(field1, field2);
		bridgeController = new BridgeController(graphBridge, null);
	}

	@Test
	public void testAddToGameField() {
		// How to test this one?
		fail("Not yet implemented");
	}

	@Test
	public void testGetGraphBridge() {
		assertTrue("Should be the same", bridgeController.getGraphBridge().equals(graphBridge));
	}

	@Test
	public void testToggleVisibility() {
		bridgeController.setVisible(true);
		assertTrue("Should be visible", bridgeController.isVisible());
		bridgeController.toggleVisibility();
		assertTrue("Shouldn't be visible now", !bridgeController.isVisible());
		bridgeController.toggleVisibility();
		assertTrue("Should be visible now", bridgeController.isVisible());
	}

}
