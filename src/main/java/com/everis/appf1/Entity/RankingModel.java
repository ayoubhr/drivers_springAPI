package com.everis.appf1.Entity;

import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@Service
public class RankingModel {

	private String name;
	private String picture;
	private String team;
	private int age;
	private Object[] races = new Object[1];

	public RankingModel() {
	}

	public String toString() {
		return "{" + "\"name\"" + ": " + "\"" + this.name + "\"" + ", " + "\"picture\"" + ": " + "\"" + this.picture
				+ "\"" + ", " + "\"team\"" + ": " + "\"" + this.team + "\"" + ", " + "\"races\"" + ": " + toJson()
				+ "}";
	}

	/**
	 * 
	 * @return
	 */
	public JsonElement toJson() {
		return JsonParser.parseString((String) this.races[0].toString());
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public String getPicture() {
		return picture;
	}

	/**
	 * 
	 * @param picture
	 */
	public void setPicture(String picture) {
		this.picture = picture;
	}

	/**
	 * 
	 * @return
	 */
	public String getTeam() {
		return team;
	}

	/**
	 * 
	 * @param team
	 */
	public void setTeam(String team) {
		this.team = team;
	}

	/**
	 * 
	 * @return
	 */
	public int getAge() {
		return age;
	}

	/**
	 * 
	 * @param age
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * 
	 * @return
	 */
	public Object[] getRaces() {
		return races;
	}

	/**
	 * 
	 * @param races
	 */
	public void setRaces(Object races) {
		this.races[0] = races;
	}
}
