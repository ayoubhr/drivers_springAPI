package com.everis.appf1;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.everis.appf1.Controller.DriverController;

@Tag("normal")
@SpringBootTest
public class AppF1ApplicationTest {

	@Autowired
	private DriverController driverController;
	
	@Test
	public void contextLoads() {
		try {
			assertThat(driverController).isNotNull();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

}