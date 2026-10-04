package com.krishagni.catissueplus.core.common.events;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.common.domain.SearchEntityKeyword;
import com.krishagni.catissueplus.core.common.util.Utility;

public class SearchResult {
	private Long id;

	private String entity;

	private Long entityId;

	private String key;

	private String value;

	private Map<String, Object> entityProps = new HashMap<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Map<String, Object> getEntityProps() {
		return entityProps;
	}

	public void setEntityProps(Map<String, Object> entityProps) {
		this.entityProps = entityProps;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SearchResult that = (SearchResult) o;
		return getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

	public static SearchResult from(SearchEntityKeyword keyword) {
		SearchResult result = new SearchResult();
		result.setId(keyword.getId());
		result.setEntity(keyword.getEntity());
		result.setEntityId(keyword.getEntityId());
		result.setKey(keyword.getKey());
		result.setValue(keyword.getValue());
		return result;
	}

	public static List<SearchResult> from(Collection<SearchEntityKeyword> keywords) {
		return Utility.nullSafeStream(keywords).map(SearchResult::from).collect(Collectors.toList());
	}
}
