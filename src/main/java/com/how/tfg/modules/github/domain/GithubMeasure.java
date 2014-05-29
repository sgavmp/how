package com.how.tfg.modules.github.domain;

import java.util.Map;

import com.how.tfg.modules.core.domain.MeasureModuleAbstract;

public class GithubMeasure extends MeasureModuleAbstract {
	
	private String repoId;
	private String repoUser;
	private String repoName;
	private Map<Long, Integer> commitsDay;

	public GithubMeasure() {

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
