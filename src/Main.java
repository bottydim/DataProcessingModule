import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Main {

	public static void main(String[] args) {
		
		String events_str = Communicator.search(0, 0, 0, 2016, 1, 30);
		ArrayList<Event> events = extractEvents(events_str);
		
		
		for (Iterator iterator = events.iterator(); iterator.hasNext();) {
			Event event = (Event) iterator.next();
			String description = event.getDescription();
			String response = Communicator.extract(description);
			try {
				event.setEntities(processResponse(response));
				for(Entity e:event.getEntities())
				{
					
					System.out.println(e);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static ArrayList<Event> extractEvents(String event_str) {
		JSONParser parser = new JSONParser();
		ArrayList<Event> events = new ArrayList<>();
//		event_str.replace("{\"result\": {\"count\": \"36313\", ", "");
		StringBuilder sb = new StringBuilder(event_str);
		String pattern = "\"event\": (\\{.*?\"granularity\": .*?\\})";
//		String pattern = "(\"event\": (\\{.*}?))";//.+?\": \".+?\",?)*\\})";
	      // Create a Pattern object
	      Pattern r = Pattern.compile(pattern);

	      // Now create matcher object.
	      Matcher m = r.matcher(event_str);
	      while(m.find()) {
	    	  
//	         System.out.println("Found value: " + m.group(1) );
	         try {
				JSONObject event_json = (JSONObject) parser.parse(m.group(1));

				LocalDate date = convertDate((String)event_json.get("date"));
				String description = (String) event_json.get("description");
				String granularity = (String) event_json.get("granularity");
				List<String> links = new ArrayList<String>();
				events.add(new Event(date,description,granularity,links));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      } 
		return events;
	}

	private static LocalDate convertDate(String object) {
	
		String[] date_parts = object.split("/");
		if(date_parts.length<3){
			Integer year = Integer.parseInt(date_parts[0]);
			return LocalDate.ofYearDay(year, 1);
		}
		else{
			Integer month = Integer.parseInt(date_parts[1]);
			Integer day = Integer.parseInt(date_parts[2]);
			return LocalDate.parse(Integer.parseInt(date_parts[0])+"-"+(month >10? month :"0"+month)+"-"+(day >10? day :"0"+day));
		}
	}

	private static List<Entity> processResponse(String response) throws ParseException {
		
		ArrayList<Entity> entities = new ArrayList<>();
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(response);
		JSONArray entitis = (JSONArray) obj.get("entities");
		for(int i=0;i<entitis.size();i++)
		{
			JSONObject data  = (JSONObject) entitis.get(i);
			String name = (String) data.get("normalized_text");
			Double score = (Double) data.get("score");
			JSONObject addInfo = (JSONObject) data.get("additional_information");
		    String type = (String) data.get("type");
		    if(type.equals("places_eng"))
		    	entities.add(new Location(name,score,addInfo));
		    if(type.equals("people_eng"))
		    	entities.add(new Person(name,score,addInfo));
			
		}
		return entities;
	}
	

}
