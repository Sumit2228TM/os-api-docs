package com.krishagni.catissueplus.core.de.events;

import java.util.Date;

import com.krishagni.catissueplus.core.common.events.UserSummary;

public class FormContextRevisionDetail {
	private Long rev;

	private Integer revType;

	private UserSummary revBy;

	private Date revTime;

	private Long id;

	private String entityType;

	private String entityName;

	public Long getRev() {
		return rev;
	}

	public void setRev(Long rev) {
		this.rev = rev;
	}

	public Integer getRevType() {
		return revType;
	}

	public void setRevType(Integer revType) {
		this.revType = revType;
	}

	public UserSummary getRevBy() {
		return revBy;
	}

	public void setRevBy(UserSummary revBy) {
		this.revBy = revBy;
	}

	public Date getRevTime() {
		return revTime;
	}

	public void setRevTime(Date revTime) {
		this.revTime = revTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
}
