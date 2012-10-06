package com.localjobs.controllers;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.localjobs.domain.Job;

@Service
public class LocalJobsServiceImpl implements LocalJobsService {

	@Inject
	private MongoTemplate mongoTemplate;
	
	@Override
	public List<Job> findAllLocalJobs() {
		Query query = new Query().limit(10);
		query.fields().exclude("description").exclude("city").exclude("country").exclude("state").exclude("expirationDate");
		return mongoTemplate.find(query, Job.class);
	}

	@Override
	public Job findOneLocalJob(int linkedInJobId) {
		Query query = Query.query(Criteria.where("linkedinJobId").is(linkedInJobId));
		query.fields().exclude("description").exclude("city").exclude("country").exclude("state").exclude("expirationDate");
		return mongoTemplate.findOne(query, Job.class);
	}

	@Override
	public List<Job> findAllLocalJobsNear(double latitude, double longitude) {
		Query query = Query.query(Criteria.where("location").near(new Point(latitude, longitude))).limit(3);
		query.fields().exclude("description").exclude("city").exclude("country").exclude("state").exclude("expirationDate");
		return mongoTemplate.find(query, Job.class);
	}

	@Override
	public List<Job> findAllLocalJobsNear(double latitude, double longitude, String skill) {
		Query query = Query.query(Criteria.where("location").near(new Point(latitude, longitude)).and("skills").regex(skill,"i")).limit(3);
		query.fields().exclude("description").exclude("city").exclude("country").exclude("state").exclude("expirationDate");
		return mongoTemplate.find(query, Job.class);
	}

}
