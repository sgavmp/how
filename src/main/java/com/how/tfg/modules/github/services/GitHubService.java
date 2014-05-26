package com.how.tfg.modules.github.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubCommit;
import org.springframework.social.github.api.GitHubIssue;
import org.springframework.social.github.api.GitHubRepo;
import org.springframework.social.github.api.GitHubStatsCommitActivity;
import org.springframework.social.github.api.GitHubStatsParticipation;
import org.springframework.social.github.api.GitHubUserProfile;
import org.springframework.stereotype.Service;

import com.how.tfg.modules.github.domain.GithubMeasure;
import com.how.tfg.modules.github.repository.GithubMeasureRepository;

@Service
public class GitHubService {
	
	private GithubMeasureRepository repository;
	
	private ConnectionRepository connectionRepository;

	@Autowired
    public GitHubService(GithubMeasureRepository repository, ConnectionRepository connectionRepository) {
        this.repository = repository;
        this.connectionRepository = connectionRepository;
    }
	
	public String getEmailOfUserLgoin() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	@SuppressWarnings("unchecked")
	public GitHub getApi() {
		return ((Connection<GitHub>) connectionRepository.findConnections("github").get(0)).getApi();
	}
	
	@SuppressWarnings("unchecked")
	public String getLogin() {
		return ((Connection<GitHub>) connectionRepository.findConnections("github").get(0)).getDisplayName();
	}
	
	public GitHubUserProfile getMeProfile() {
		return getApi().userOperations().getUserProfile();
	}
	
	public List<GitHubRepo> getAllRepo() {
		String user = getLogin();
		return Arrays.asList(getApi().restOperations().getForObject(buildUri("/users/" + user + "/repos"), GitHubRepo[].class));
	}
	
	public List<GithubMeasure> getAllMeasure() {
		return repository.getByEmail(getEmailOfUserLgoin());
	}
	
	// Using String here instead of URI so I can include braces in the path. See, e.g., RepoTemplate. [WLW]
	protected String buildUri(String path) {
//		return URIBuilder.fromUri(API_URL_BASE + path).build();
		return API_URL_BASE + path;
	}
	
	// GitHub API v3
	private static final String API_URL_BASE = "https://api.github.com/";

	public boolean notExistRepoMeasure(String repoName) {
		return repository.getByEmailAndRepoName(getEmailOfUserLgoin(), repoName).size()==0;
	}

	public void createMeasureOfRepodId(String repoName) {
		GitHubRepo repo = getApi().repoOperations().getRepo(getLogin(), repoName);
		GithubMeasure measure = new GithubMeasure();
		measure.setRepoId(repo.getId().toString());
		measure.setRepoName(repo.getName());
		measure.setRepoUser(getLogin());
		measure.setEmail(getEmailOfUserLgoin());
		List<GitHubStatsCommitActivity> stats = getStatsCommitActivity(getLogin(), repoName);
		Collections.sort(stats);
		Map<Long,Integer> commitPerDay = new TreeMap<Long,Integer>();
//		DateTime initYear = new DateTime(DateTime.now().getYear(), 1, 1, 0, 0);
		for (GitHubStatsCommitActivity stat : stats) {
			if (stat.getTotal()!=0) {
				DateTime day = new DateTime(stat.getWeek()*1000);
				for (Integer n : stat.getDays()) {
					if (n!=0) {
						commitPerDay.put(day.getMillis(), n);
					}
					day = day.plusDays(1);
				}
			}
		}
		measure.setCommitsDay(commitPerDay);
		repository.save(measure);
	}
	
	public void refreshMeasure(String measureId) {
		GithubMeasure measure = repository.findOne(measureId);
		List<GitHubStatsCommitActivity> stats = getStatsCommitActivity(measure.getRepoUser(), measure.getRepoName());
		Collections.sort(stats);
		Map<Long,Integer> commitPerDay = new TreeMap<Long,Integer>();
		for (GitHubStatsCommitActivity stat : stats) {
			if (stat.getTotal()!=0) {
				DateTime day = new DateTime(stat.getWeek()*1000);
				for (Integer n : stat.getDays()) {
					if (n!=0) {
						commitPerDay.put(day.getMillis(), n);
					}
					day = day.plusDays(1);
				}
			}
		}
		measure.setCommitsDay(commitPerDay);
		repository.save(measure);
	}

	public void deleteMeasureOfId(String measureid) {
		repository.delete(measureid);
	}
	
	public List<GitHubIssue> getAllIssuesForRepository(String name) {
		return getApi().repoOperations().getIssues(getLogin(), name);
	}
	
	public List<GitHubCommit> getAllCommitForRepository(String name) {
		return getApi().repoOperations().getCommits(getLogin(), name);
	}
	
	public List<GitHubStatsCommitActivity> getStatsCommitActivity(String user, String name) {
		return getApi().statsOperations().getCommitActivity(user, name);
	}
	
	public GitHubStatsParticipation getStatsParticipation(String user, String name) {
		return getApi().statsOperations().getParticipation(user, name);
	}

	public GithubMeasure getBoardMeasureById(String measureid) {
		return repository.findOne(measureid);
	}
	
	public boolean haveConnection() {
		return connectionRepository.findConnections("github").size()>0; 
	}
}
