package com.krishagni.catissueplus.core.de.events;


import java.util.List;

//
// either (formId, recordId) => single record
// or (formId, entityType, objectId/s) => multiple records
//
public class FormRecordCriteria {
	private Long formId;
	
	private Long recordId;

	private String entityType;

	private List<Long> objectIds;

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public List<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(List<Long> objectIds) {
		this.objectIds = objectIds;
	}
}
