package com.how.tfg.modules.core.domain;

import javax.persistence.Id;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

public abstract class MeasureModuleAbstract {
	
	@Id
	protected String id;
	@CreatedDate
	protected DateTime dateCreation;
	@LastModifiedDate
	protected DateTime updateCreation;
	protected String email;

	public MeasureModuleAbstract() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	
	public DateTime getUpdateCreation() {
		return updateCreation;
	}

	public void setUpdateCreation(DateTime updateCreation) {
		this.updateCreation = updateCreation;
	}

}
