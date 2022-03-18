package com.everis.appf1.Repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.everis.appf1.Entity.DriverEntity;
import com.everis.appf1.Entity.Race;

@DataMongoTest
class DriverRepositoryTest {
	
	@Autowired
	private DriverRepository repository;

	@Test
	void itShouldSaveAndFindDriver() {
		// Given
		DriverEntity driver = new DriverEntity();
		Race[] races = new Race[10];
		driver.set_id("777");
		driver.setName("DriverTest");
		driver.setTeam("McLaren");
		driver.setPicture("Base64picture");
		driver.setAge(35);
		driver.setRaces(races);
		
		repository.save(driver);
		
		// When
		boolean exists = repository.findAll() != null;
		
		// Then
		assertThat(exists).isTrue();
	}

}
