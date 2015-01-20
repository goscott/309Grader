package model.roster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A score for an assignment, with sub-assignments
 * @author Gavin Scott
 */
public class ScoreNode implements Serializable {
	/**
     * generated serial ID
     */
    private static final long serialVersionUID = 3189041012899607191L;
    private ArrayList<ScoreNode> subnodes;
	private String name;
	private double value;
	
	/**
	 * Creates a score node with the given
	 * information
	 * @param name the name of the node
	 * @param value the value of the node
	 */
	public ScoreNode(String name, double value) {
		this.name = name;
		this.value = value;
		subnodes = new ArrayList<ScoreNode>();
	}
	
	/**
	 * Adds a sub-node with the given information
	 * @param name the name 
	 * @param value the value
	 */
	public void addSub(String name, double value) {
		subnodes.add(new ScoreNode(name, value));
	}
	
	/**
	 * Gets the name of the node
	 * @return String the name
	 */
	public String name() {
		return name;
	}
	
	/**
	 * Gets the value of the node
	 * @return double the value
	 */
	public double value() {
		return value;
	}
	 /**
	  * Gets a list of the node's children
	  * @return The list of children
	  */
	public ArrayList<ScoreNode> getChildren() {
		return subnodes;
	}

	/**
	 * Saves a list of score nodes
	 * @param scores the scores to save
	 * @return a String representing the scores
	 */
    public static String Save(HashMap<String, ScoreNode> scores)
    {
        //TODO save the nodes
        return "";
    }

	/**
	 * Save a score node
	 * @param scores the score to save
	 * @return a String representing the score
	 */
    public static String Save(ScoreNode score)
    {
        // TODO Auto-generated method stub
        return "";
    }
}
