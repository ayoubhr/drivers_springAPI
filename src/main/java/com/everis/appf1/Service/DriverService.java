package com.everis.appf1.Service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.stereotype.Service;
import com.everis.appf1.Entity.DriverEntity;
import com.everis.appf1.Entity.RankingModel;
import com.everis.appf1.Repository.DriverRepository;
import org.springframework.boot.configurationprocessor.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@Service
public class DriverService {
	
	private final DriverRepository repository;
	
	// RankingModel represents the way data is responded back to the client. Do not mistake with DriverEntity class, 
	// which is a map of how the data is retrieved from the mongoDB database before processing it for requests.
	private RankingModel rm;
	
	/**
	 * 
	 * @param repository
	 */
	public DriverService(DriverRepository repository, RankingModel rm) {
		this.repository = repository;
		this.rm = rm;
	}
	
	/**
	 *  CRUD operation to find all pilots from the database.
	 * @return List<DriverEntity>
	 */
	public List<DriverEntity> findAll() {
		  return repository.findAll();
	}
	
	/**
	 *  
	 * @param time
	 * @return
	 */
	public double convertTimeToSecs(String time) {
		
		// Self explanatory service, we convert the received string of time into seconds and respond back the result.
		String[] hourMinSec = time.split(":");
	    int hour = Integer.parseInt(hourMinSec[0]);
	    int mins = Integer.parseInt(hourMinSec[1]);
	    double secs =Double.parseDouble(hourMinSec[2]);
	    
	    int hoursInSecs = hour * 3600;
	    int minsInSecs = mins * 60;
	    
	    double totalSecs = hoursInSecs + minsInSecs + secs;
	    
	    return totalSecs;
	}
	
	private JsonElement getSortedTimes(JsonObject[] req_dd) throws JSONException {
		
		// This method receives an array of objects that represents the requested data, we sort the pilots through the "timer" property previously added
		// pilot with less seconds is first, pilot with the most seconds is last basically.
	    List<JsonObject> list = new ArrayList<>();
	    for (int i = 0; i < req_dd.length; i++) {
	        list.add(req_dd[i].getAsJsonObject());
	    }
	    list.sort((a1, a2) -> {
	        try {
	            return Integer.compare(a1.get("timer").getAsInt(), a2.get("timer").getAsInt());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return 0;
	    });
	    
	    return new Gson().toJsonTree(list);
	}
	
	/**
	 * 
	 * @param je
	 * @return
	 */
	private JsonObject[] sumOfRaceTimes(JsonElement[] je) {

		int[] timers = new int[je.length];
		JsonObject[] drivers = new JsonObject[je.length];

		for (int i = 0; i < je.length; i++) {
			JsonObject object = je[i].getAsJsonObject(); // <-- Accesses the corresponding pilot object on the specified array position i.

			if (object.get("races") instanceof JsonArray) {
				JsonArray races = object.get("races").getAsJsonArray(); // <-- This gets us the array of races for that specified object.

				for (int j = 0; j < races.size(); j++) {
					String time = races.get(j).getAsJsonObject().get("time").getAsString(); // <-- As there is 10 races per pilot, we iterate through them for each pilot to sum them all.
					timers[i] = (int) (timers[i] + convertTimeToSecs(time));
				}
				object.addProperty("timer", timers[i]); // <-- Once we have the full timer of races sumed up in seconds, we add that property to the pilot object.
				drivers[i] = object;
				
			} else if (object.get("races") instanceof JsonObject) { // <-- This condition is only met when the calling service is a pilot object with one single race on the response back. (getDriversByRace())
				JsonObject races = object.get("races").getAsJsonObject();

				for (int j = 0; j < 1; j++) {
					String time = races.get("time").getAsString();

					timers[i] = (int) (timers[i] + convertTimeToSecs(time));
				}
				object.addProperty("timer", timers[i]);
				drivers[i] = object;
			}
		}
		return drivers;
	}
	
	/**
	 *  Return on this method represents a JSON array.
	 * @return
	 * @throws JSONException
	 */
	public JsonElement getDrivers() throws JSONException {

		List<DriverEntity> drs = findAll();
		Object[] arr = new Object[22];
		JsonElement[] requested_data = new JsonElement[22];

		try {
			// Iterate through the list of pilots obtained from the database (List<DriverEntity> drs), one pilot per loop. 
			// We create a json object and process the data.
			
			// for each pilot we grab the name, picture, team and the array of races, set it on the RankingModel object
			// and then parse it as an element in array requested_data.
			for (int i = 0; i < drs.size(); i++) {
				String json = new Gson().toJson(drs.get(i));
				JSONObject object = new JSONObject(json);

				String name = object.getString("name");
				String picture = object.getString("picture");
				String team = object.getString("team");
				JSONArray races = (JSONArray) object.get("races");
				arr[i] = races;

				rm.setName(name);
				rm.setPicture(picture);
				rm.setTeam(team);
				rm.setRaces(arr[i]);

				requested_data[i] = JsonParser.parseString(rm.toString());
			}
		} catch (JSONException je) {
			System.out.println(je.getMessage());
		} catch (JsonParseException jpe) {
			System.out.println(jpe.getMessage());
		} catch (JsonSyntaxException jse) {
			System.out.println(jse.getMessage());
		}

		// With this method we get the sum of all race times returned in seconds, for each driver.
		JsonObject[] req = sumOfRaceTimes(requested_data); 
		// With this methid we get drivers sorted according to their global ranking.
		JsonElement req_d = getSortedTimes(req);
		
		// On this loop we add the position property to the JSON according to the previous methods returns.
		for(int i=0; i<req.length; i++) {
			req_d.getAsJsonArray().get(i).getAsJsonObject().addProperty("position", i+1);
		}
		
		//If Requested data isnt null, we create the JSON tree and send back the response to the client.
		JsonElement response = null;
		if(req_d != null) {
			response = new Gson().toJsonTree(req_d);
		}
		return response;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws JSONException
	 */
	public JsonElement getDriversByRace(int id) throws JSONException {
		
		List<DriverEntity> drs = findAll();
		Object[] arr = new Object[22];
		JsonElement[] requested_data = new JsonElement[22];

		try {
			// Same process as the previous method where we get the whole list of drivers sent back to the client.
			// With a slight twist, as getDrivers() responds back with a JSON array object then a JsonArray of the data
			// is created before we iterate through it to get the info the client requested.
			JsonElement drivers = getDrivers();
			JsonArray data = new Gson().fromJson(drivers, JsonArray.class);

			for (int i = 0; i < drs.size(); i++) {
				String json = new Gson().toJson(drs.get(i));
				JSONObject object = new JSONObject(json);

				String name = object.getString("name");
				String picture = object.getString("picture");
				String team = object.getString("team");

				// data.get(i).getAsJsonObject gets us the driver as an object from the JsonArray data
				// then .get("races").getAsJsonArray gets us the array of races thats sitting within the drivers JsonObject
				// then .get(id = name of the race) gets us the specified race we are looking for as an object.
				arr[i] = data.get(i).getAsJsonObject().get("races").getAsJsonArray().get(id).getAsJsonObject();

				rm.setName(name);
				rm.setPicture(picture);
				rm.setTeam(team);
				rm.setRaces(arr[i]);

				requested_data[i] = JsonParser.parseString(rm.toString());
			}
		} catch (JSONException je) {
			System.out.println(je.getMessage());
		}  catch (JsonParseException jpe) {
			System.out.println(jpe.getMessage());
		} catch (JsonSyntaxException jse) {
			System.out.println(jse.getMessage());
		} 
		
		// With this method we get the sum of the specified race time returned in seconds, for each driver.
		JsonObject[] req = sumOfRaceTimes(requested_data);
		// With this methid we get drivers sorted according to their specific race ranking.
		JsonElement req_d = getSortedTimes(req);
		
		// On this loop we add the position property to the JSON according to the previous methods returns.
		for(int i=0; i<req.length; i++) {
			req_d.getAsJsonArray().get(i).getAsJsonObject().addProperty("position", i+1);
		}

		JsonElement response = null;
		if (req_d != null) {
			response = new Gson().toJsonTree(req_d);
		}
		return response;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws JSONException
	 */
	public JsonElement getDriverInfo(String id) throws JSONException{
		
		List<DriverEntity> drs = findAll();
		JsonArray arr = null;
		// Since we are looking only for one of the drivers information, requested_data on this service has only 1 position
		// unlike previous services where we had 22 positions on the array to represent each one of the drivers.
		JsonElement[] requested_data = new JsonElement[1];
		
		try {
			JsonElement drivers = getDrivers();
			JsonArray data = new Gson().fromJson(drivers, JsonArray.class);
			
			for(int i=0; i<data.size(); i++) {
				String json = new Gson().toJson(drs.get(i));
				JSONObject object = new JSONObject(json);
				String name = object.getString("name");
				
				if(name.equals(id)) {
					String picture = object.getString("picture");
					String team = object.getString("team");
					int age = object.getInt("age");
					
					arr = data.get(i).getAsJsonObject().get("races").getAsJsonArray();
					
					rm.setName(name);
					rm.setPicture(picture);
					rm.setTeam(team);
					rm.setAge(age);
					rm.setRaces(arr);
					
					// As opposed to the previous services, here we dont use rm.toString() directly as there is a slight change on the request,
					// we are requested the age property aswell, that property is not mapped on the original RankingModel toString() method
					// as its a unique situation. Notice that the following String produces the exact same result as the toString method
					// implemented on RankingModel with the added age property.
					String format = "{" + "\"name\"" + ": " + "\"" + rm.getName() + "\"" + ", " + "\"age\"" + ": "
							+ rm.getAge() + ", " + "\"picture\"" + ": " + "\"" + rm.getPicture() + "\"" + ", "
							+ "\"team\"" + ": " + "\"" + rm.getTeam() + "\"" +  ", " + "\"races\"" + ": " + rm.toJson()
							+"}";
					
					requested_data[0] = JsonParser.parseString(format);
				}
			}
		} catch (JSONException je) {
			System.out.println(je.getMessage());
		}  catch (JsonParseException jpe) {
			System.out.println(jpe.getMessage());
		} catch (JsonSyntaxException jse) {
			System.out.println(jse.getMessage());
		} 

		JsonObject[] req_d = sumOfRaceTimes(requested_data);
		JsonElement global = getDrivers();
		
		// Right before we respond back the requested data, on this specific service as we only have the timer property injected from the previous "sumOfRaceTimes"
		// We have to add the position property "manually", we iterate through the JSON array object requested from getDrivers and see which one of the pilots
		// coincides with the one we are processing, through the name property. Once detected, we add the proper timer and position to it, position on this context
		// represents the users global ranking, to display on profile info on the FrontEnd in this case.
		int number_of_pilots = global.getAsJsonArray().size();
		
		for(int i = 0; i<number_of_pilots; i++) {
			if(global.getAsJsonArray().get(i).getAsJsonObject().get("name").getAsString().equals(req_d[0].getAsJsonObject().get("name").getAsString())) {
				
				int timer = global.getAsJsonArray().get(i).getAsJsonObject().get("timer").getAsInt();
				int position = global.getAsJsonArray().get(i).getAsJsonObject().get("position").getAsInt();
				
				req_d[0].getAsJsonObject().remove("timer");
				req_d[0].getAsJsonObject().addProperty("timer", timer);
				req_d[0].getAsJsonObject().addProperty("position", position);
			}
		}
		
		// Like in the previous loop where we added the global ranking of the driver, here we are adding the ranking of the driver
		// for each specific race. We call the "getDriversByRace(race id)" that responds us with an array of all the drivers for the
		// specified race, then match the driver we are targetting through the "name" property and then extract his position for
		// each one of the races.
		int number_of_races = req_d[0].getAsJsonObject().get("races").getAsJsonArray().size();
		
		for (int i = 0; i < number_of_pilots; i++) {
			for (int j = 0; j < number_of_races; j++) {
				JsonElement driversByRace = getDriversByRace(j);
				JsonArray dataByRace = new Gson().fromJson(driversByRace, JsonArray.class);

				String pilot_name = dataByRace.getAsJsonArray().get(i).getAsJsonObject().get("name").getAsString();
				
				if (pilot_name.equals(req_d[0].getAsJsonObject().get("name").getAsString())) {
					int race_position = dataByRace.getAsJsonArray().get(i).getAsJsonObject().get("position").getAsInt();
					req_d[0].getAsJsonObject().get("races").getAsJsonArray().get(j).getAsJsonObject()
							.addProperty("position", race_position);
				}
			}
		}
		
		JsonElement response = null;
		if (req_d != null) {
			response = new Gson().toJsonTree(req_d); 
		}
		return response;
	}
}

