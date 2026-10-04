package com.krishagni.catissueplus.core.common.events;

import java.util.List;

public class BulkEntityDetail<T> {
	private T detail;

	private List<Long> ids;

	private List<String> names;

	public T getDetail() {
		return detail;
	}

	public void setDetail(T detail) {
		this.detail = detail;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}
}
