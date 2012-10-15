package com.localjobs.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@ComponentScan(basePackages = "com.localjobs")
public class ApplicationConfig {

	@Inject
	MongoDbFactoryConfig mongoDbFactoryConfig;

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(
				mongoDbFactoryConfig.mongoDbFactory());
		return mongoTemplate;
	}

}
