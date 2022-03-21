package com.everis;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.everis.appf1.AppF1ApplicationTest;
import com.everis.appf1.Repository.DriverRepositoryTest;
import com.everis.appf1.Service.DriverServiceTest;

@RunWith(Suite.class)
@SuiteClasses({ AppF1ApplicationTest.class, DriverRepositoryTest.class, DriverServiceTest.class })
public class AllTests {
	
}
