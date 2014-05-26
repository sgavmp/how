package com.how.tfg.data.modules.github.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.how.tfg.data.domain.User;

public class GithubMeasure {

	@Id
	private String id;
	private String repoId;
	private String repoUser;
	private String repoName;
	@CreatedDate
	private DateTime dateCreation;
	@LastModifiedDate
	private DateTime updateCreation;
	private String email;
	private Map<Long, Integer> commitsDay;

	public GithubMeasure() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DateTime getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(DateTime dateCreation) {
		this.dateCreation = dateCreation;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public DateTime getUpdateCreation() {
		return updateCreation;
	}

	public void setUpdateCreation(DateTime updateCreation) {
		this.updateCreation = updateCreation;
	}

	public String getRepoId() {
		return repoId;
	}

	public void setRepoId(String repoId) {
		this.repoId = repoId;
	}

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public Map<Long, Integer> getCommitsDay() {
		return commitsDay;
	}

	public void setCommitsDay(Map<Long, Integer> commitsDay) {
		this.commitsDay = commitsDay;
	}

	public String getRepoUser() {
		return repoUser;
	}

	public void setRepoUser(String repoUser) {
		this.repoUser = repoUser;
	}
	
}
