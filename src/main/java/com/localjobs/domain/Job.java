package com.localjobs.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Document(collection = "jobs")
public class Job {

	@Id
	private String id;

	private CompanyInformation company;

	private String jobTitle;

	private double[] location;

	private String[] skills;

	private String formattedAddress;

	public Job() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CompanyInformation getCompany() {
		return company;
	}

	public void setCompany(CompanyInformation company) {
		this.company = company;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setLocation(double[] location) {
		this.location = location;
	}

	public double[] getLocation() {
		return location;
	}

	public void setSkills(String[] skills) {
		this.skills = skills;
	}

	public String[] getSkills() {
		return skills;
	}

	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

	public String getFormattedAddress() {
		return formattedAddress;
	}

	public String toJson() {
		return new JSONSerializer().include("location").include("company")
				.include("skills").exclude("*.class").serialize(this);
	}

	public static Job fromJson(String json) {
		return new JSONDeserializer<Job>().use(null, Job.class).deserialize(
				json);
	}

	public static String toJsonArray(Collection<Job> collection) {
		return new JSONSerializer().include("location").include("company")
				.include("skills").exclude("*.class").serialize(collection);
	}

	public static Collection<Job> fromJsonArray(String json) {
		return new JSONDeserializer<List<Job>>().use(null, ArrayList.class)
				.use("location", Job.class).deserialize(json);
	}

	@Override
	public String toString() {
		return "Job [id=" + id + ", company=" + company + ", jobTitle="
				+ jobTitle + ", location=" + Arrays.toString(location)
				+ ", skills=" + Arrays.toString(skills) + ", formattedAddress="
				+ formattedAddress + "]";
	}

	
	
}
