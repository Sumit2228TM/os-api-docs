package com.krishagni.catissueplus.core.common.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class Notification extends BaseEntity {
	private String operation;

	private String entityType;

	private Long entityId;

	private String message;

	private User createdBy;
	
	private Date creationTime;
	
	private Set<UserNotification> notifiedUsers = new HashSet<>();

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Set<UserNotification> getNotifiedUsers() {
		return notifiedUsers;
	}

	public void setNotifiedUsers(Set<UserNotification> notifiedUsers) {
		this.notifiedUsers = notifiedUsers;
	}
}
