package com.localjobs.service;

import java.util.List;

import com.localjobs.domain.Job;

public interface LocalJobsService {
	List<Job> findAllLocalJobs();

	Job findOneLocalJob(String jobId);

	List<Job> findAllLocalJobsNear(double latitude, double longitude);

	List<Job> findAllLocalJobsNear(double latitude, double longitude,
			String name);
}
