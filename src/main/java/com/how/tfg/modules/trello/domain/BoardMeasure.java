package com.how.tfg.modules.trello.domain;

import java.util.Map;

import javax.persistence.Id;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

public class BoardMeasure {

	@Id
	private String id;
	private String boardId;
	private String name;
	@CreatedDate
	private DateTime dateCreation;
	@LastModifiedDate
	private DateTime updateCreation;
	private String email;
	private Map<String, String> listName;
	private Map<String, Map<Long,Integer>> taskForList;

	public BoardMeasure() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBoardId() {
		return boardId;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Map<String, Map<Long, Integer>> getTaskForList() {
		return taskForList;
	}

	public void setTaskForList(Map<String, Map<Long, Integer>> taskForList) {
		this.taskForList = taskForList;
	}
	
	public DateTime getUpdateCreation() {
		return updateCreation;
	}

	public void setUpdateCreation(DateTime updateCreation) {
		this.updateCreation = updateCreation;
	}

	public Map<String, String> getListName() {
		return listName;
	}

	public void setListName(Map<String, String> listName) {
		this.listName = listName;
	}
}
