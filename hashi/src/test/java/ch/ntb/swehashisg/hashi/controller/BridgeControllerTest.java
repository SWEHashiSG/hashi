package ch.ntb.swehashisg.hashi.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import ch.ntb.swehashisg.hashi.graph.GraphDasFactory;
import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;

public class BridgeControllerTest {

	GameFieldController gameFieldController;
	BridgeController bridgeController;
	GraphBridge graphBridge;
	GraphField field1, field2;
	GameFieldController gameField;

	@Before
	public void preparations() {
		field1 = new GraphField(1, 2, 2);
		field2 = new GraphField(2, 2, 2);
		gameField = new GameFieldPlayController(GraphDasFactory.getEmptyGraphDas(6, 6), null);
		graphBridge = new GraphBridge(field1, field2);

		bridgeController = new BridgeController(graphBridge, gameField);
	}

	@Test
	public void testAddToGameField() {
		try {
			field1 = new GraphField(1, 2, 2);
			field2 = new GraphField(2, 2, 5);
			graphBridge = new GraphBridge(field1, field2);
			bridgeController = new BridgeController(graphBridge, gameField);
			bridgeController.addToGameField();
			fail("Dit not throw an Exception");
		} catch (IllegalArgumentException e) {
			// Expected Exception if bridge Length is 0
		}
		field1 = new GraphField(1, 2, 2);
		field2 = new GraphField(3, 2, 5);
		graphBridge = new GraphBridge(field1, field2);
		bridgeController = new BridgeController(graphBridge, gameField);
		bridgeController.addToGameField();
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
