package com.everis.appf1.Service;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.configurationprocessor.json.JSONException;

import com.everis.appf1.Entity.RankingModel;
import com.everis.appf1.Repository.DriverRepository;

class DriverServiceTest {
	
	@Mock
	private DriverRepository repository;
	private AutoCloseable autoCloseable;
	private RankingModel rm;
	private DriverService service;

	@BeforeEach
	void setUp() throws Exception {
		autoCloseable = MockitoAnnotations.openMocks(this);
		service = new DriverService(repository, rm);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		autoCloseable.close();
	}

	@Test
	void testGetDrivers() throws JSONException {
		service.findAll();
		verify(repository).findAll();
	}
	
	@Test
	@Disabled
	void testGetDriverInfo() {
		
	}

}
