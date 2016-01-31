import org.json.simple.JSONObject;


public abstract class Entity {

	protected String name;
	protected double score;
	protected JSONObject addInfo;
	
	public Entity(String name, Double score, JSONObject addInfo) {
		this.name = name;
		this.score = score;
		this.addInfo = addInfo;
	}

	@Override
	public String toString() {
		return this.getClass().getName()+" [name=" + name + ", score=" + score + ", addInfo="
				+ addInfo + "]";
	}
	
	
}
