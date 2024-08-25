package com.devops.accomodation_service;

import com.devops.accomodation_service.repository.AccomodationRepository;
import com.devops.accomodation_service.service.AccomodationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccomodationServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@InjectMocks
	private AccomodationService accomodationService;

	@Mock
	private AccomodationRepository accomodationRepository;

	@BeforeEach
	void setUp() {

	}

	@Test
	void testGetAccomodation() {

	}
}
