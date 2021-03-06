package com.how.tfg.modules.github.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.how.tfg.modules.github.domain.GithubMeasure;

public interface GithubMeasureRepository extends MongoRepository<GithubMeasure, String> {
	
	public List<GithubMeasure> getByEmail(String email);
	public List<GithubMeasure> getByEmailAndRepoName(String emailOfUserLgoin, String repoName);

}
