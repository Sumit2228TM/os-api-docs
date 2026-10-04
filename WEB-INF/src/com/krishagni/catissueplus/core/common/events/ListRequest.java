package com.krishagni.catissueplus.core.common.events;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishagni.catissueplus.core.query.Column;

public class ListRequest {
	private List<Column> filters;

	private Column orderBy;


	public List<Column> filters() {
		return filters;
	}

	@JsonProperty("filters")
	public ListRequest filters(List<Column> filters) {
		this.filters = filters;
		return this;
	}

	public Column orderBy() {
		return orderBy;
	}

	@JsonProperty("orderBy")
	public ListRequest orderBy(Column orderBy) {
		this.orderBy = orderBy;
		return this;
	}
}
