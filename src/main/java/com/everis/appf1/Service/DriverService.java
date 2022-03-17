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
	private RankingModel rm;
	
	/**
	 * 
	 * @param repository
	 */
	DriverService(DriverRepository repository, RankingModel rm) {
		this.repository = repository;
		this.rm = rm;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<DriverEntity> findAll() {
		  return repository.findAll();
	}
	
	private JsonElement getSortedTimes(JsonObject[] req_dd) throws JSONException {
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
	 * @param time
	 * @return
	 */
	private double convertTimeToSecs(String time) {
		String[] hourMinSec = time.split(":");
	    int hour = Integer.parseInt(hourMinSec[0]);
	    int mins = Integer.parseInt(hourMinSec[1]);
	    double secs =Double.parseDouble(hourMinSec[2]);
	    
	    int hoursInSecs = hour * 3600;
	    int minsInSecs = mins * 60;
	    
	    double totalSecs = hoursInSecs + minsInSecs + secs;
	    
	    return totalSecs;
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
			JsonObject object = je[i].getAsJsonObject(); // <-- Accede al objeto piloto especificado en la posicion del array

			if (object.get("races") instanceof JsonArray) {
				JsonArray races = object.get("races").getAsJsonArray(); // <-- devuelve el array de carreras del piloto especificado

				for (int j = 0; j < races.size(); j++) {
					String time = races.get(j).getAsJsonObject().get("time").getAsString();
					/* System.out.println(time); */ // <-- devuelve el tiempo de la carrera en la posicion j del array de races
					timers[i] = (int) (timers[i] + convertTimeToSecs(time));
				}
				object.addProperty("timer", timers[i]);
				drivers[i] = object;
				
			} else if (object.get("races") instanceof JsonObject) {
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
	 * 
	 * @return
	 * @throws JSONException
	 */
	public JsonElement getDrivers() throws JSONException {

		List<DriverEntity> drs = findAll();
		Object[] arr = new Object[22];
		JsonElement[] requested_data = new JsonElement[22];

		try {
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
			JsonElement drivers = getDrivers();
			JsonArray data = new Gson().fromJson(drivers, JsonArray.class);

			for (int i = 0; i < drs.size(); i++) {
				String json = new Gson().toJson(drs.get(i));
				JSONObject object = new JSONObject(json);

				String name = object.getString("name");
				String picture = object.getString("picture");
				String team = object.getString("team");

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
		
		JsonObject[] req = sumOfRaceTimes(requested_data);
		JsonElement req_d = getSortedTimes(req);
		
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
		
		for(int i = 0; i<22; i++) {
			
			if(global.getAsJsonArray().get(i).getAsJsonObject().get("name").getAsString().equals(req_d[0].getAsJsonObject().get("name").getAsString())) {
				
				int timer = global.getAsJsonArray().get(i).getAsJsonObject().get("timer").getAsInt();
				int position = global.getAsJsonArray().get(i).getAsJsonObject().get("position").getAsInt();
				
				req_d[0].getAsJsonObject().remove("timer");
				req_d[0].getAsJsonObject().addProperty("timer", timer);
				req_d[0].getAsJsonObject().addProperty("position", position);
			}
		}
		
		JsonElement response = null;
		if (req_d != null) {
			response = new Gson().toJsonTree(req_d);
		}
		return response;
	}
}

