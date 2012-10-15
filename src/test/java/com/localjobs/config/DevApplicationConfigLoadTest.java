package com.localjobs.config;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
@ActiveProfiles("dev")
public class DevApplicationConfigLoadTest {

	@Inject
	MongoTemplate mongoTemplate;

	@Test
	public void checkIfApplicationContextIsLoadedFine() {
		assertNotNull(mongoTemplate);
	}

}
