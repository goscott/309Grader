package roster;

import java.util.List;

/**
 * A grade-able assignment
 * @author Gavin Scott
 */
public class GradedItem {
	private String name;
	private String descr;
	private ScoreNode score;
	
	public GradedItem(String name, String descr) {
		this(name, descr, null);
	}
	
	public GradedItem(String name, String descr, ScoreNode score) {
		this.name = name;
		this.descr = descr;
		this.score = score;
	}
	
	public String name() {
		return name;
	}
	
	public String descr() {
		return descr;
	}
	
	public ScoreNode score() {
		return score;
	}
	
	public void changeScore(ScoreNode sc) {
		score = sc;
	}
	
	public boolean equals(Object other) {
		if((other != null) && (other instanceof GradedItem)) {
			GradedItem oth = (GradedItem)other;
			return oth.name().equals(name);
		}
		return false;
	}
	
	public GradedItem copy() {
		return new GradedItem(name, descr);
	}

    public static String Save(List<GradedItem> assignments)
    {
        String toReturn = "";
        char secret = 1;
        for(GradedItem item : assignments) {
            toReturn += "A" +secret;
            toReturn += item.name + secret + item.descr + secret + ScoreNode.Save(item.score);
            toReturn +="\n";
        }
        return toReturn;
    }
}
