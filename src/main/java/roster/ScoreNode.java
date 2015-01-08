package roster;

import java.util.ArrayList;

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
	
	public String getName() {
		return name;
	}
	
	public double getValue() {
		return value;
	}
	
	public ArrayList<ScoreNode> getChildren() {
		return subnodes;
	}
}
