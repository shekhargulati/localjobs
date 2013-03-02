package com.localjobs.service;

import java.util.List;

import com.localjobs.domain.Job;

public interface LocalJobsService {
	List<Job> findAllLocalJobs();

	Job findOneLocalJob(String jobId);

	List<Job> findAllLocalJobsNear(double longitude,double latitude);

	List<Job> findAllLocalJobsNear(double longitude,double latitude, 
			String name);
}
