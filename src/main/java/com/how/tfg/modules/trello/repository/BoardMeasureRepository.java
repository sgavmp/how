package com.how.tfg.modules.trello.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.how.tfg.modules.trello.domain.BoardMeasure;

public interface BoardMeasureRepository extends MongoRepository<BoardMeasure, String> {
	
	public List<BoardMeasure> getByEmail(String email);
	public List<BoardMeasure> getByEmailAndBoardId(String email,String boardId);

}
