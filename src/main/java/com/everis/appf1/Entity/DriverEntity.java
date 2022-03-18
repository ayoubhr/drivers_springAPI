package com.everis.appf1.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "data_dos")
public class DriverEntity {

	private String _id;
	private String picture;
	private int age;
	private String name;
	private String team;
	private Race[] races = new Race[10];

	public DriverEntity() {
	}

	/**
	 * 
	 * @param d
	 */
	public DriverEntity(DriverEntity d) {
		this._id = d._id;
		this.picture = d.picture;
		this.age = d.age;
		this.name = d.name;
		this.team = d.team;
		this.races = d.races;
	}

	/**
	 * returns a JsonArray formation for the model DriverEntity, which maps the way the objects are stored on the mongoDB collection/documents.
	 * 
	 * @param int i
	 */
	public String toString(int i) {
		return "[{" + "\"_id\"" + ": " + "\"" + this._id + "\"" + ", " + "\"picture\"" + ": " + "\"" + this.picture
				+ "\"" + ", " + "\"age\"" + ": " + "\"" + this.age + "\"" + ", " + "\"name\"" + ": " + "\"" + this.name
				+ "\"" + ", " + "\"team\"" + ": " + "\"" + this.team + "\"" + ", " + "\"races\"" + ": " + "[" + "\n"
				+ "{ " + "\n" + "\"name\"" + ": " + "\"" + this.races[i].getName() + "\"" + ", " + "\"time\"" + ": "
				+ "\"" + this.races[i].getTime() + "\"" + "\n" + "}, " + "\n" + "{ " + "\n" + "\"name\"" + ": " + "\""
				+ this.races[i + 1].getName() + "\"" + ", " + "\"time\"" + ": " + "\"" + this.races[i + 1].getTime()
				+ "\"" + "\n" + "}, " + "\n" + "{ " + "\n" + "\"name\"" + ": " + "\"" + this.races[i + 2].getName()
				+ "\"" + ", " + "\"time\"" + ": " + "\"" + this.races[i + 2].getTime() + "\"" + "\n" + "}, " + "\n"
				+ "{ " + "\n" + "\"name\"" + ": " + "\"" + this.races[i + 3].getName() + "\"" + ", " + "\"time\"" + ": "
				+ "\"" + this.races[i + 3].getTime() + "\"" + "\n" + "}, " + "\n" + "{ " + "\n" + "\"name\"" + ": "
				+ "\"" + this.races[i + 4].getName() + "\"" + ", " + "\"time\"" + ": " + "\""
				+ this.races[i + 4].getTime() + "\"" + "\n" + "}, " + "\n" + "{ " + "\n" + "\"name\"" + ": " + "\""
				+ this.races[i + 5].getName() + "\"" + ", " + "\"time\"" + ": " + "\"" + this.races[i + 5].getTime()
				+ "\"" + "\n" + "}, " + "\n" + "{ " + "\n" + "\"name\"" + ": " + "\"" + this.races[i + 6].getName()
				+ "\"" + ", " + "\"time\"" + ": " + "\"" + this.races[i + 6].getTime() + "\"" + "\n" + "}, " + "\n"
				+ "{ " + "\n" + "\"name\"" + ": " + "\"" + this.races[i + 7].getName() + "\"" + ", " + "\"time\"" + ": "
				+ "\"" + this.races[i + 7].getTime() + "\"" + "\n" + "}, " + "\n" + "{ " + "\n" + "\"name\"" + ": "
				+ "\"" + this.races[i + 8].getName() + "\"" + ", " + "\"time\"" + ": " + "\""
				+ this.races[i + 8].getTime() + "\"" + "\n" + "}, " + "\n" + "{ " + "\n" + "\"name\"" + ": " + "\""
				+ this.races[i + 9].getName() + "\"" + ", " + "\"time\"" + ": " + "\"" + this.races[i + 9].getTime()
				+ "\"" + "\n" + "} " + "\n" + "]" + "}]";
	}

	/**
	 * 
	 * @return
	 */
	public String get_id() {
		return _id;
	}

	/**
	 * 
	 * @param _id
	 */
	public void set_id(String _id) {
		this._id = _id;
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
	public Race[] getRaces() {
		return races;
	}

	/**
	 * 
	 * @param races
	 */
	public void setRaces(Race[] races) {
		this.races = races;
	}
}
