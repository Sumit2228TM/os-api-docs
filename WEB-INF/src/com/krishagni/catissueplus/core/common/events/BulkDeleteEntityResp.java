package com.krishagni.catissueplus.core.common.events;

import java.util.List;

public class BulkDeleteEntityResp<T> {
	private boolean completed;

	private List<T> entities;

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public List<T> getEntities() {
		return entities;
	}

	public void setEntities(List<T> entities) {
		this.entities = entities;
	}
}
