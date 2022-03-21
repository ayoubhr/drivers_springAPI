package com.everis.appf1.Repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.everis.appf1.Entity.DriverEntity;
import com.everis.appf1.Entity.Race;

@DataMongoTest
public class DriverRepositoryTest {
	
	@Autowired
	private DriverRepository repo;

	@Test
	public void itShouldSaveAndFindDriver() {
		// Given
		DriverEntity driver = new DriverEntity();
		Race[] races = new Race[10];
		driver.set_id("777");
		driver.setName("DriverTest");
		driver.setTeam("McLaren");
		driver.setPicture("Base64picture");
		driver.setAge(35);
		driver.setRaces(races);
		
		repo.save(driver);
		
		// When
		boolean exists = repo.findAll() != null;
		
		// Then
		assertThat(exists).isTrue();
	}

}
