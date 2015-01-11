package roster;

import java.util.ArrayList;

/**
 * A score for an assignment, with sub-assignments
 * @author Gavin Scott
 */
public class ScoreNode {
	private ArrayList<ScoreNode> subnodes;
	private String name;
	private double value;
	
	public ScoreNode(String name, double value) {
		this.name = name;
		this.value = value;
		subnodes = new ArrayList<ScoreNode>();
	}
	
	public void addSub(String name, double val) {
		subnodes.add(new ScoreNode(name, val));
	}
	
	public String name() {
		return name;
	}
	
	public double value() {
		return value;
	}
	
	public ArrayList<ScoreNode> getChildren() {
		return subnodes;
	}
}
