import java.time.LocalDate;
import java.util.List;

import org.json.simple.JSONArray;


public class Event {

	protected List<Entity> entities;
	protected LocalDate date;
	protected String description;
	protected String granularity;
	protected List<String> links;
	public Event(String description) {
		this.description = description;
	}

	public Event(LocalDate date, String description, String granularity,
			List<String> links) {
		super();
		this.date = date;
		this.description = description;
		this.granularity = granularity;
		this.links = links;
	}


	public String getDescription() {
		return this.description;
	}

	public void setEntities(List<Entity> entities) {
	
			this.entities = entities;
		
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	
}
