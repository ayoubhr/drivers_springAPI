package com.everis.appf1.Controller;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.everis.appf1.Service.DriverService;
import com.google.gson.JsonElement;

@RestController
@CrossOrigin(origins = "*")
public class DriverController {

	private final DriverService service;

	/**
	 * 
	 * @param service
	 */
	DriverController(DriverService service) {
		this.service = service;
	}

	@GetMapping("/ranking")
	public JsonElement findAll() throws JSONException {
		return service.getDrivers();
	}
	
	@GetMapping("/ranking-gp")
	@ResponseBody
	public JsonElement findDriversByRace(@RequestParam int id) throws JSONException {
		return service.getDriversByRace(id);
	}
	
	@GetMapping("/driver")
	@ResponseBody
	public JsonElement findDriverInfo(@RequestParam String id) throws JSONException {
		return service.getDriverInfo(id);
	}
}
