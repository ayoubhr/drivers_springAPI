package com.everis.appf1.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.everis.appf1.Entity.RankingModel;
import com.everis.appf1.Repository.DriverRepository;

@DataMongoTest
public class DriverServiceTest {
	
	@Mock
	private DriverRepository repository;
	private AutoCloseable autoCloseable;
	private RankingModel rm;
	
	private DriverService service;

	@BeforeEach
	public void setUp() throws Exception {
		autoCloseable = MockitoAnnotations.openMocks(this);
		service = new DriverService(repository, rm);
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		autoCloseable.close();
	}

	@Test
	public void testGetDrivers() throws JSONException {
		service.findAll();
		verify(repository).findAll();
	}
	
	@Test
	public void isTimeCorrect() throws JSONException {
		// given 
		String time = "1:11:39.515";
		int expected = 4299;
		// when
		int total = (int) service.convertTimeToSecs(time);
		// then
		assertThat(total).isEqualTo(expected);
	}

}
