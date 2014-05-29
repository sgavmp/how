package com.how.tfg.modules.trello.domain;

import java.util.Map;

import com.how.tfg.modules.core.domain.MeasureModuleAbstract;


public class BoardMeasure extends MeasureModuleAbstract {
	
	private String boardId;
	private String name;
	private Map<String, String> listName;
	private Map<String, Map<Long,Integer>> taskForList;

	public BoardMeasure() {

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

	public Map<String, Map<Long, Integer>> getTaskForList() {
		return taskForList;
	}

	public void setTaskForList(Map<String, Map<Long, Integer>> taskForList) {
		this.taskForList = taskForList;
	}
	

	public Map<String, String> getListName() {
		return listName;
	}

	public void setListName(Map<String, String> listName) {
		this.listName = listName;
	}
}
