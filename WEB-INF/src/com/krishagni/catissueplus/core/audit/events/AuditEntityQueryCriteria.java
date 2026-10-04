package com.krishagni.catissueplus.core.audit.events;

public class AuditEntityQueryCriteria {
	private String objectName;

	private Long objectId;

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}
}
