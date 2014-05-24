package com.how.tfg.data.domain.trello;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

public interface BoardMeasureRepository extends MongoRepository<BoardMeasure, String> {
	
	public List<BoardMeasure> getByEmail(String email);

}
