package com.krishagni.catissueplus.core.administrative.domain;

import com.krishagni.catissueplus.core.common.events.OpenSpecimenEvent;

import krishagni.catissueplus.beans.FormRecordEntryBean;

public class FormDataDeleteEvent extends OpenSpecimenEvent {

	private String entityType;

	private Object object;

	public FormDataDeleteEvent(String entityType, Object object, FormRecordEntryBean eventData) {
		super(null, eventData);
		this.entityType = entityType;
		this.object = object;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
