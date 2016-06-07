package ch.ntb.swehashisg.hashi.graph;

import ch.ntb.swehashisg.hashi.model.GraphBridge;
import ch.ntb.swehashisg.hashi.model.GraphField;
import ch.ntb.swehashisg.hashi.model.GraphPlayField;

/**
 * Provides an interface for persisting a {@link GraphPlayField graphPlayField}
 *
 */
public interface GraphDas {

	/**
	 * Gets the actual {@link GraphPlayField graphPlayField} from the
	 * graphDatabase.
	 * 
	 * @return the actual {@link GraphPlayField graphPlayField}
	 */
	public GraphPlayField getPlayField();

	/**
	 * Sets the needed {@link GraphBridge bridges} of a {@link GraphField
	 * graphField}.
	 * 
	 * @param field
	 *            the {@link GraphField graphField} to set the needed
	 *            {@link GraphBridge bridges}
	 */
	public void setBridges(GraphField field);

	/**
	 * Adds a {@link GraphBridge bridge} to the {@link GraphPlayField
	 * graphFields}. The {@link GraphField graphFields} need to be neighbors,
	 * the {@link GraphField graphFields} need each another {@link GraphBridge
	 * bridge} and {@link GraphBridge bridges} aren't allow to cross.
	 * 
	 * @param bridge
	 *            the {@link GraphBridge bridge} to add
	 */
	public void addBridge(GraphBridge bridge);

	/**
	 * Removes a {@link GraphBridge bridge} from the graphPlayField. There must
	 * be at least one bridge between to two graphFields.
	 * 
	 * @param bridge
	 *            the {@link GraphBridge bridge} to remove
	 */
	public void removeBridge(GraphBridge bridge);

	/**
	 * Adds a {@link GraphBridge solutionBridge} to the {@link GraphPlayField
	 * graphPlayField}. The {@link GraphField graphFields} need to be neighbors,
	 * the {@link GraphField graphFields} need each another {@link GraphBridge
	 * solutionBridge} and {@link GraphBridge solutionBridges} aren't allow to
	 * cross.
	 * 
	 * @param bridge
	 *            the {@link GraphBridge solutionBridge} to add
	 */
	public void addSolutionBridge(GraphBridge bridge);

	/**
	 * Removes a {@link GraphBridge solutionBridge} from the
	 * {@link GraphPlayField graphPlayField}. There must be at least one
	 * {@link GraphBridge solutionBridge}bridge between to two graphFields.
	 * 
	 * @param bridge
	 *            the {@link GraphBridge solutionBridge} to remove
	 */
	public void removeSolutionBridge(GraphBridge bridge);
}
