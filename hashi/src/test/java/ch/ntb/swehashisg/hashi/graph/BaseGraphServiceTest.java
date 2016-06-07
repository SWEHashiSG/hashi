package ch.ntb.swehashisg.hashi.graph;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;

@RunWith(MockitoJUnitRunner.class)
public class BaseGraphServiceTest {

	@Mock
	private GraphDas graphDas;

	@InjectMocks
	private BaseGraphService baseGraphService;

	@Test
	public void testGetPlayField() {
		GraphPlayField graphPlayField = new GraphPlayField(new HashSet<>(), new HashSet<>(), new HashSet<>(), 0, 0);
		when(graphDas.getPlayField()).thenReturn(graphPlayField);
		assertTrue("GraphBaseService doesn't call graphDas!", baseGraphService.getPlayField() == graphPlayField);
	}

	@Test
	public void testSetBridges() {
		GraphField graphField = new GraphField(0, 0);
		baseGraphService.setBridges(graphField);
		verify(graphDas).setBridges(graphField);
	}

	@Test
	public void testIsFinished() {
		Set<GraphField> graphFields = new HashSet<>();
		GraphField graphField = mock(GraphField.class);
		graphFields.add(graphField);
		GraphPlayField graphPlayField = mock(GraphPlayField.class);
		when(graphPlayField.getFields()).thenReturn(graphFields);
		when(graphField.getBridges()).thenReturn(2);
		List<GraphBridge> bridges = new ArrayList<>();
		GraphBridge graphBridge = mock(GraphBridge.class);
		bridges.add(graphBridge);
		bridges.add(graphBridge);
		when(graphField.getExistingBridges()).thenReturn(bridges);
		assertTrue("Should be finished!", baseGraphService.isFinished(graphPlayField));
	}

	@Test
	public void testAddBridge() {
		GraphField field1 = spy(new GraphField(1, 1, 1));
		Set<GraphField> neighbors = new HashSet<>();
		GraphField field2 = new GraphField(1, 3, 1);
		neighbors.add(field2);
		when(field1.getNeighbors()).thenReturn(neighbors);
		GraphBridge graphBridge = new GraphBridge(field1, field2);
		baseGraphService.addBridge(graphBridge);
		verify(graphDas).addBridge(graphBridge);
	}

	@Test
	public void testAddSolutionBridge() {
		GraphField field1 = spy(new GraphField(1, 1, 1));
		Set<GraphField> neighbors = new HashSet<>();
		GraphField field2 = new GraphField(1, 3, 1);
		neighbors.add(field2);
		when(field1.getNeighbors()).thenReturn(neighbors);
		GraphBridge graphBridge = new GraphBridge(field1, field2);
		baseGraphService.addSolutionBridge(graphBridge);
		verify(graphDas).addSolutionBridge(graphBridge);
	}

	@Test
	public void testRemoveBridge() {
		GraphField field1 = spy(new GraphField(1, 1, 1));
		List<GraphBridge> existingBridges = new ArrayList<>();
		Set<GraphField> neighbors = new HashSet<>();
		GraphField field2 = spy(new GraphField(1, 3, 1));
		neighbors.add(field2);
		when(field1.getNeighbors()).thenReturn(neighbors);
		GraphBridge graphBridge = new GraphBridge(field1, field2);
		existingBridges.add(graphBridge);
		when(field1.getExistingBridges()).thenReturn(existingBridges);
		when(field2.getExistingBridges()).thenReturn(existingBridges);
		baseGraphService.removeBridge(graphBridge);
		verify(graphDas).removeBridge(graphBridge);
	}

	@Test
	public void testRemoveSolutionBridge() {
		GraphField field1 = spy(new GraphField(1, 1, 1));
		List<GraphBridge> existingBridges = new ArrayList<>();
		Set<GraphField> neighbors = new HashSet<>();
		GraphField field2 = spy(new GraphField(1, 3, 1));
		neighbors.add(field2);
		when(field1.getNeighbors()).thenReturn(neighbors);
		GraphBridge graphBridge = new GraphBridge(field1, field2);
		existingBridges.add(graphBridge);
		when(field1.getExistingBridges()).thenReturn(existingBridges);
		when(field2.getExistingBridges()).thenReturn(existingBridges);
		baseGraphService.removeSolutionBridge(graphBridge);
		verify(graphDas).removeSolutionBridge(graphBridge);
	}

	@Test
	public void testRestart() {
		GraphPlayField graphPlayField = mock(GraphPlayField.class);
		Set<GraphBridge> graphBridges = new HashSet<>();
		GraphBridge graphBridge = mock(GraphBridge.class);
		graphBridges.add(graphBridge);
		when(graphPlayField.getBridges()).thenReturn(graphBridges);
		when(graphBridge.getWeighting()).thenReturn(2);
		baseGraphService.restart(graphPlayField);
		verify(graphDas, times(2)).removeBridge(graphBridge);
	}

}
