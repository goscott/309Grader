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
	
	public String name() {
		return name;
	}
	
	public String descr() {
		return descr;
	}
	
	public ScoreNode score() {
		return score;
	}
}
