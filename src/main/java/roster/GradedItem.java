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
}
