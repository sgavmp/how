package com.how.tfg.data.domain.trello;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import org.joda.time.DateTime;

import com.how.tfg.data.domain.User;

public class BoardMeasure {

	@Id
	private String id;
	private String boardId;
	private String name;
	private DateTime dateCreation;
	private String email;
	private Map<String, String> lists;
	private Map<String, String> tasks;
	private Map<DateTime, Map<String, Integer>> taskForList;
	
	public BoardMeasure() {
		
	}
	
	public BoardMeasure(String id, String boardId, String name,
			DateTime dateCreation, String email, Map<String, String> lists,
			Map<String, String> tasks,
			Map<DateTime, Map<String, Integer>> taskForList) {
		super();
		this.id = id;
		this.boardId = boardId;
		this.name = name;
		this.dateCreation = dateCreation;
		this.email = email;
		this.lists = lists;
		this.tasks = tasks;
		this.taskForList = taskForList;
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

	public Map<String, String> getLists() {
		return lists;
	}

	public void setLists(Map<String, String> lists) {
		this.lists = lists;
	}

	public Map<String, String> getTasks() {
		return tasks;
	}

	public void setTasks(Map<String, String> tasks) {
		this.tasks = tasks;
	}

	public Map<DateTime, Map<String, Integer>> getTaskForList() {
		return taskForList;
	}

	public void setTaskForList(Map<DateTime, Map<String, Integer>> taskForList) {
		this.taskForList = taskForList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boardId == null) ? 0 : boardId.hashCode());
		result = prime * result
				+ ((dateCreation == null) ? 0 : dateCreation.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lists == null) ? 0 : lists.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((taskForList == null) ? 0 : taskForList.hashCode());
		result = prime * result + ((tasks == null) ? 0 : tasks.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoardMeasure other = (BoardMeasure) obj;
		if (boardId == null) {
			if (other.boardId != null)
				return false;
		} else if (!boardId.equals(other.boardId))
			return false;
		if (dateCreation == null) {
			if (other.dateCreation != null)
				return false;
		} else if (!dateCreation.equals(other.dateCreation))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lists == null) {
			if (other.lists != null)
				return false;
		} else if (!lists.equals(other.lists))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (taskForList == null) {
			if (other.taskForList != null)
				return false;
		} else if (!taskForList.equals(other.taskForList))
			return false;
		if (tasks == null) {
			if (other.tasks != null)
				return false;
		} else if (!tasks.equals(other.tasks))
			return false;
		return true;
	}
	
}
