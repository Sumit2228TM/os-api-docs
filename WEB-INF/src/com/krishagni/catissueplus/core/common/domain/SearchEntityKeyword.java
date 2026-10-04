package com.krishagni.catissueplus.core.common.domain;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class SearchEntityKeyword extends BaseEntity {
	private String entity;

	private Long entityId;

	private String key;

	private String value;

	//
	// 1: alive
	// 0: deleted
	//
	private int status;

	//
	// indicates whether to delete the keyword
	//
	private transient int op;

	private transient String oldValue;

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getOp() {
		return op;
	}

	public void setOp(int op) {
		this.op = op;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public void update(SearchEntityKeyword other) {
		setValue(other.getValue());
		setStatus(other.getStatus());
	}

	@Override
	public String toString() {
		return "SearchEntityKeyword{" +
			"entity='" + entity + '\'' +
			", entityId=" + entityId +
			", key='" + key + '\'' +
			", value='" + value + '\'' +
			", status=" + status +
			", op=" + op +
			", oldValue='" + oldValue + '\'' +
			'}';
	}
}
