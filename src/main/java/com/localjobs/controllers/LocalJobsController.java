package com.localjobs.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.localjobs.domain.Job;
import com.localjobs.domain.LocalJobWithDistance;
import com.localjobs.googleapis.DistanceResponse;
import com.localjobs.googleapis.GoogleDistanceClient;
import com.localjobs.service.CoordinateFinder;
import com.localjobs.service.LocalJobsService;
import org.springframework.http.MediaType;


@Controller
public class LocalJobsController {

	@Inject
	private LocalJobsService localJobsService;

	@Inject
	private GoogleDistanceClient googleDistanceClient;

	@Inject
	private CoordinateFinder coordinateFinder;

	@RequestMapping("/jobs")
	public ResponseEntity<String> allJobs() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<Job> jobs = localJobsService.findAllLocalJobs();
		return new ResponseEntity<String>(Job.toJsonArray(jobs), headers,
				HttpStatus.OK);
	}

	@RequestMapping("/jobs/{jobId}")
	public ResponseEntity<String> oneJob(@PathVariable("jobId") String jobId) {
		Job job = localJobsService.findOneLocalJob(jobId);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (job == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(job.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping("/jobs/near")
	public String allJobsNearToLatitudeAndLongitude(
			@RequestParam("longitude") double longitude,
			@RequestParam("latitude") double latitude, Model model) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<Job> jobs = localJobsService.findAllLocalJobsNear(longitude,latitude);
		List<LocalJobWithDistance> localJobsWithDistance = new ArrayList<LocalJobWithDistance>();
		for (Job localJob : jobs) {
			DistanceResponse response = googleDistanceClient.findDirections(
					localJob.getLocation(),
					new double[] { latitude, longitude });
			LocalJobWithDistance linkedinJobWithDistance = new LocalJobWithDistance(
					localJob, response.rows[0].elements[0].distance,
					response.rows[0].elements[0].duration);
			localJobsWithDistance.add(linkedinJobWithDistance);
		}

		model.addAttribute("jobs", localJobsWithDistance);
		return "jobs";
	}

	@RequestMapping("/jobs/near/{skill}")
	public String allJobsNearLatitideAndLongitudeWithSkill(
			@PathVariable("skill") String skill,
			@RequestParam("longitude") double longitude, @RequestParam("latitude") double latitude, Model model) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<LocalJobWithDistance> localJobsWithDistance = findJobs(skill, longitude,latitude);
		model.addAttribute("jobs", localJobsWithDistance);
		return "jobs";
	}

	@RequestMapping(value = "/jobs/near/{location}/{skill}",produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<LocalJobWithDistance> allJobsNearToLocationWithSkill(
			@PathVariable("location") String location,
			@PathVariable("skill") String skill, Model model) throws Exception {
		long startTime = System.currentTimeMillis();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		long startTimeCoordinateFinder = System.currentTimeMillis();
		double[] coordinates = {-118.404471, 33.978622}; 
				//coordinateFinder.find(location);
		long endTimeCoordinateFinder = System.currentTimeMillis();
		System.out.println("Total time taken by CoordinateFinder : "
				+ (endTimeCoordinateFinder - startTimeCoordinateFinder) / 1000
				+ " seconds");

		if (ArrayUtils.isEmpty(coordinates)) {
			return null;
		}

		double latitude = coordinates[0];
		double longitude = coordinates[1];
		List<LocalJobWithDistance> localJobsWithDistance = findJobs(skill,
				longitude,latitude);
		model.addAttribute("jobs", localJobsWithDistance);
		long endTime = System.currentTimeMillis();
		System.out
				.println("Total time taken to get allJobsNearToLocationWithSkill() : "
						+ (endTime - startTime) / 1000 + " seconds");
		return localJobsWithDistance;
	}

	private List<LocalJobWithDistance> findJobs(String skill,
			double longitude,double latitude) {
		long startTimeMongoDB = System.currentTimeMillis();
		List<Job> jobs = localJobsService.findAllLocalJobsNear(
				longitude, latitude, skill);
		long endTimeMongoDB = System.currentTimeMillis();

		System.out.println("Time taken by MongoDB : "
				+ (endTimeMongoDB - startTimeMongoDB) / 1000 + " seconds");
		List<LocalJobWithDistance> locaJobsWithDistance = new ArrayList<LocalJobWithDistance>();
		long startTimeGoogleDistaceClient = System.currentTimeMillis();
		for (Job localJob : jobs) {
			DistanceResponse response = googleDistanceClient.findDirections(
					localJob.getLocation(),
					new double[] { latitude, longitude });
			LocalJobWithDistance linkedinJobWithDistance = new LocalJobWithDistance(
					localJob, response.rows[0].elements[0].distance,
					response.rows[0].elements[0].duration);
			locaJobsWithDistance.add(linkedinJobWithDistance);
		}
		long endTimeGoogleDistanceClient = System.currentTimeMillis();
		System.out.println("Time taken by GoogleDistanceClient : "
				+ (endTimeGoogleDistanceClient - startTimeGoogleDistaceClient)
				/ 1000 + " seconds");
		return locaJobsWithDistance;
	}
}