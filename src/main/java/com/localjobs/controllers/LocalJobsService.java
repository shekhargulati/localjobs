package com.localjobs.controllers;

import java.util.List;

import com.localjobs.domain.Job;

public interface LocalJobsService {
	List<Job> findAllLocalJobs();

	Job findOneLocalJob(int linkedInId);

	List<Job> findAllLocalJobsNear(double latitude, double longitude);

	List<Job> findAllLocalJobsNear(double latitude, double longitude, String name);
}
