package com.localjobs.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.Mongo;

@Configuration
@Profile("dev")
@PropertySource("classpath:com/localjobs/config/dev.properties")
public class DevMongoDBFactoryConfig implements MongoDbFactoryConfig {

	@Inject
	Environment environment;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openshift.notebook.core.config.MongoDbConfig#mongoDbFactory()
	 */
	@Override
	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {
		Mongo mongo = new Mongo(environment.getProperty("mongo.host"),
				Integer.parseInt(environment.getProperty("mongo.port")));
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongo,
				environment.getProperty("mongo.db"));
		return mongoDbFactory;
	}

}
