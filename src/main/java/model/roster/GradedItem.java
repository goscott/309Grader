package model.roster;

import java.util.ArrayList;
import java.util.List;

/**
 * A grade-able assignment, and a category in the gradebook
 * @author Gavin Scott
 */
public class GradedItem {
	private String name;
	private String descr;
	private double score;
	private ArrayList<GradedItem> children;
	private GradedItem parent;

	public GradedItem(String name, String descr) {
		this(name, descr, null, 0);
	}
	
	public GradedItem(String name, String descr, double score) {
		this(name, descr, null, score);
	}

	public GradedItem(String name, String descr, GradedItem parent) {
		this(name, descr, parent, 0);
	}

	public GradedItem(String name, String descr, GradedItem parent, double score) {
		this.name = name;
		this.descr = descr;
		this.score = score;
		this.parent = parent;
		if(parent != null) {
			parent.addChild(this);
		}
		children = new ArrayList<GradedItem>();
	}

	public String name() {
		return name;
	}

	public String descr() {
		return descr;
	}

	public double score() {
		return score;
	}

	public void changeScore(double sc) {
		score = sc;
	}

	public void addChild(GradedItem item) {
		children.add(item);
	}

	public void removeChild(GradedItem item) {
		if (children.contains(item)) {
			item.parent = null;
			children.remove(item);
		}
	}

	public boolean leaf() {
		return children.isEmpty();
	}

	public boolean hasParent() {
		return parent != null;
	}

	public void setParent(GradedItem newParent) {
		parent.removeChild(this);
		parent = newParent;
		newParent.addChild(this);
	}

	public double calcScore() {
		double sc = 0;
		for (GradedItem item : children) {
			sc += item.score();
		}
		return sc;
	}

	public boolean equals(Object other) {
		if ((other != null) && (other instanceof GradedItem)) {
			GradedItem oth = (GradedItem) other;
			return oth.name().equals(name) && oth.descr().equals(descr)
					&& (oth.score() == score);
		}
		return false;
	}

	public GradedItem copy() {
		return new GradedItem(name, descr, score);
	}

	public static String Save(List<GradedItem> assignments) {
		String toReturn = "";
		char secret = 1;
		for (GradedItem item : assignments) {
			toReturn += "A" + secret;
			toReturn += item.name + secret + item.descr + secret + item.score()
					+ secret;
			// + ScoreNode.Save(item.score);
			toReturn += "\n";
		}
		return toReturn;
	}
}
