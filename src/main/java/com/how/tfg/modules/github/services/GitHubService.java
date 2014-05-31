package com.how.tfg.modules.github.services;

import java.io.IOException;
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
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubRepo;
import org.springframework.social.github.api.GitHubStatsCommitActivity;
import org.springframework.social.github.api.GitHubUserProfile;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.how.tfg.modules.core.services.ServiceModuleAbstract;
import com.how.tfg.modules.github.domain.GithubMeasure;
import com.how.tfg.modules.github.repository.GithubMeasureRepository;

@Service
public class GitHubService extends ServiceModuleAbstract<GitHub, GithubMeasure> {
	
	private GithubMeasureRepository repository;

	@Autowired
	public GitHubService(ConnectionRepository connectionRepository, GithubMeasureRepository repository, UsersConnectionRepository userConnectionRepository) {
        super(connectionRepository, repository, userConnectionRepository, "github");
        this.repository = repository;
    }
	
	public String getEmailOfUserLgoin() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
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
		if (stats!=null) {
			if (stats.size()>1) {
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
				if (commitPerDay.isEmpty()) {
					commitPerDay.put(DateTime.now().getMillis(), 0);
				}
			}
		}
		measure.setCommitsDay(commitPerDay);
		repository.save(measure);
	}
	
	public void refreshMeasure(String measureId) {
		GithubMeasure measure = repository.findOne(measureId);
		this.refreshMeasure(measure);
	}
	
	public void refreshMeasure(GithubMeasure measure) {
		List<GitHubStatsCommitActivity> stats = getStatsCommitActivity(measure.getRepoUser(), measure.getRepoName());
		Collections.sort(stats);
		Map<Long,Integer> commitPerDay = new TreeMap<Long,Integer>();
		if (stats!=null) {
			if (stats.size()>1) {
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
				if (commitPerDay.isEmpty()) {
					commitPerDay.put(DateTime.now().getMillis(), 0);
				}
			}
		}
		measure.setCommitsDay(commitPerDay);
		repository.save(measure);
	}
	
	public List<GitHubStatsCommitActivity> getStatsCommitActivity(String user, String name) {
		return getStatsCommitActivity(user, name, getApi());
	}
	
	public List<GitHubStatsCommitActivity> getStatsCommitActivity(String user, String name, GitHub api) {
		return api.statsOperations().getCommitActivity(user, name);
	}

	@Override
	public void refreshMeasureOffline(GithubMeasure measure) {
		GitHub api = super.getApiOfMeasureUser(measure);
		List<GitHubStatsCommitActivity> stats = getStatsCommitActivity(measure.getRepoUser(), measure.getRepoName(), api);
		Collections.sort(stats);
		Map<Long,Integer> commitPerDay = new TreeMap<Long,Integer>();
		if (stats!=null) {
			if (stats.size()>1) {
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
				if (commitPerDay.isEmpty()) {
					commitPerDay.put(DateTime.now().getMillis(), 0);
				}
			}
		}
		measure.setCommitsDay(commitPerDay);
		repository.save(measure);
	}
}
