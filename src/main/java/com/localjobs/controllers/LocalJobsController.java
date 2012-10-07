package com.localjobs.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.localjobs.domain.Job;
import com.localjobs.googleapis.DistanceResponse;
import com.localjobs.googleapis.GoogleDistanceClient;
import com.localjobs.service.LocalJobsService;
import com.localjobs.utils.CoordinateFinder;
import com.localjobs.vo.LocalJobWithDistance;

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
    return new ResponseEntity<String>(Job.toJsonArray(jobs), headers, HttpStatus.OK);
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
  public ResponseEntity<String> allJobsNearToLatitudeAndLongitude(@RequestParam("latitude") double latitude,
      @RequestParam("longitude") double longitude) {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");

    List<Job> jobs = localJobsService.findAllLocalJobsNear(latitude, longitude);
    List<LocalJobWithDistance> localJobsWithDistance = new ArrayList<LocalJobWithDistance>();
    for (Job localJob : jobs) {
      DistanceResponse response =
          googleDistanceClient.findDirections(localJob.getLocation(), new double[] { latitude, longitude });
      LocalJobWithDistance linkedinJobWithDistance =
          new LocalJobWithDistance(localJob, response.rows[0].elements[0].distance,
            response.rows[0].elements[0].duration);
      localJobsWithDistance.add(linkedinJobWithDistance);
    }

    return new ResponseEntity<String>(LocalJobWithDistance.toJsonArray(localJobsWithDistance), headers, HttpStatus.OK);
  }

  @RequestMapping("/jobs/near/{skill}")
  public ResponseEntity<String> allJobsNearLatitideAndLongitudeWithSkill(@PathVariable("skill") String skill,
      @RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude) {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    List<LocalJobWithDistance> locaJobsWithDistance = findJobs(skill, latitude, longitude);
    return new ResponseEntity<String>(LocalJobWithDistance.toJsonArray(locaJobsWithDistance), headers, HttpStatus.OK);
  }

  @RequestMapping("/jobs/near/{location}/{skill}")
  public ResponseEntity<String> allJobsNearToLocationWithSkill(@PathVariable("location") String location,
      @PathVariable("skill") String skill) throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    double[] coordinates = coordinateFinder.find(location);
    if (ArrayUtils.isEmpty(coordinates)) {
      return new ResponseEntity<String>("Not able to understand the address", headers, HttpStatus.BAD_REQUEST);
    }

    double latitude = coordinates[0];
    double longitude = coordinates[1];
    List<LocalJobWithDistance> localJobsWithDistance = findJobs(skill, latitude, longitude);
    return new ResponseEntity<String>(LocalJobWithDistance.toJsonArray(localJobsWithDistance), headers, HttpStatus.OK);
  }

  private List<LocalJobWithDistance> findJobs(String skill, double latitude, double longitude) {
    List<Job> jobs = localJobsService.findAllLocalJobsNear(latitude, longitude, skill);
    List<LocalJobWithDistance> locaJobsWithDistance = new ArrayList<LocalJobWithDistance>();
    for (Job localJob : jobs) {
      DistanceResponse response =
          googleDistanceClient.findDirections(localJob.getLocation(), new double[] { latitude, longitude });
      LocalJobWithDistance linkedinJobWithDistance =
          new LocalJobWithDistance(localJob, response.rows[0].elements[0].distance,
            response.rows[0].elements[0].duration);
      locaJobsWithDistance.add(linkedinJobWithDistance);
    }
    return locaJobsWithDistance;
  }
}