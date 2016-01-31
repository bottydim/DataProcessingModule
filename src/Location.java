import org.json.simple.JSONObject;


public class Location extends Entity {

	private double lon;
	private double lat;
	public Location(String name, Double score, JSONObject addInfo) {
		super(name, score, addInfo);
		this.lon = new Double(addInfo.get("lon")+"");
		this.lat = new Double(addInfo.get("lat")+"");
	}
	
	
}
