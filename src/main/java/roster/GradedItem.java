package roster;

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
	
	public String getName() {
		return name;
	}
	
	public String getDescr() {
		return descr;
	}
	
	public ScoreNode getScore() {
		return score;
	}
}
