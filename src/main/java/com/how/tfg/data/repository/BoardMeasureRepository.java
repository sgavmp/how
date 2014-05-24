package com.how.tfg.data.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.how.tfg.data.domain.trello.BoardMeasure;

public interface BoardMeasureRepository extends MongoRepository<BoardMeasure, String> {
	
	public List<BoardMeasure> getByEmail(String email);

}
