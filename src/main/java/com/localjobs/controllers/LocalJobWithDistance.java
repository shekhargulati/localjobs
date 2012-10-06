package com.localjobs.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.localjobs.domain.Job;
import com.localjobs.googleapis.Distance;
import com.localjobs.googleapis.Duration;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class LocalJobWithDistance {

	private Job linkedinJob;

	private Distance distance;

	private Duration duration;

	public LocalJobWithDistance() {
		// TODO Auto-generated constructor stub
	}

	public LocalJobWithDistance(Job linkedinJob, Distance distance,
			Duration duration) {
		this.linkedinJob = linkedinJob;
		this.distance = distance;
		this.duration = duration;
	}

	public Job getLinkedinJob() {
		return linkedinJob;
	}

	public void setLinkedinJob(Job linkedinJob) {
		this.linkedinJob = linkedinJob;
	}

	public Distance getDistance() {
		return distance;
	}

	public void setDistance(Distance distance) {
		this.distance = distance;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public String toJson() {
		return new JSONSerializer().include("linkedinJob").include("distance")
				.include("duration").exclude("*.class").serialize(this);
	}

	public static LocalJobWithDistance fromJson(String json) {
		return new JSONDeserializer<LocalJobWithDistance>()
				.use(null, LocalJobWithDistance.class)
				.use("linkedinJob", Job.class)
				.use("distance", Distance.class)
				.use("duration", Duration.class).deserialize(json);
	}

	public static String toJsonArray(
			Collection<LocalJobWithDistance> collection) {
		return new JSONSerializer().include("linkedinJob").include("distance")
				.include("duration").exclude("*.class").serialize(collection);
	}

	public static Collection<LocalJobWithDistance> fromJsonArray(String json) {
		return new JSONDeserializer<List<LocalJobWithDistance>>()
				.use(null, ArrayList.class)
				.use("linkedinJob", Job.class)
				.use("distance", Distance.class)
				.use("duration", Duration.class).deserialize(json);
	}

}
