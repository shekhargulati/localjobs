package com.localjobs.controllers;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoResult;
import org.springframework.data.mongodb.core.geo.GeoResults;
import org.springframework.data.mongodb.core.geo.Metric;
import org.springframework.data.mongodb.core.geo.Metrics;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.localjobs.domain.Job;

@Controller
@RequestMapping("/api/jobs")
public class JobController {

	@Inject
	MongoTemplate mongoTemplate;

	public JobController() {
	}

	@RequestMapping(value = "/{skills}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<GeoResult<Job>> allJobsNearToLocationWithSkill(
			@PathVariable("skills") String[] skills,@RequestParam("longitude") double longitude, @RequestParam("latitude") double latitude, Model model)
			throws Exception {
		System.out.println("skills " + Arrays.toString(skills));
		Metric metric = Metrics.KILOMETERS;
		Query skillsWhereClause = Query.query(Criteria.where("skills").in(skills));
		NearQuery nearQuery = NearQuery
				.near(new Point(longitude, latitude), metric)
				.query(skillsWhereClause)
				.num(10);
		GeoResults<Job> geoResults = mongoTemplate
				.geoNear(nearQuery, Job.class);
		List<GeoResult<Job>> jobs = geoResults.getContent();
		System.out.println("jobs found " + jobs.size());
		return jobs;
	}

}
