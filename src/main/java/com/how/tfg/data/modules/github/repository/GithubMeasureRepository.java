package com.how.tfg.data.modules.github.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.social.connect.Connection;

import com.how.tfg.data.modules.github.domain.GithubMeasure;

public interface GithubMeasureRepository extends MongoRepository<GithubMeasure, String> {
	
	public List<GithubMeasure> getByEmail(String email);
	public List<GithubMeasure> getByEmailAndRepoName(String emailOfUserLgoin, String repoName);

}
