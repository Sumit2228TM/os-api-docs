package com.krishagni.catissueplus.core.common.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.persister.entity.EntityPersister;

import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.domain.SearchEntityKeyword;
import com.krishagni.catissueplus.core.common.service.SearchEntityKeywordProvider;

public abstract class AbstractSearchEntityKeywordProvider implements SearchEntityKeywordProvider {
	@Override
	public List<SearchEntityKeyword> getKeywords(PostInsertEvent event) {
		return getKeywords(event.getEntity(), event.getPersister(), null, event.getState());
	}

	@Override
	public List<SearchEntityKeyword> getKeywords(PostUpdateEvent event) {
		return getKeywords(event.getEntity(), event.getPersister(), event.getOldState(), event.getState());
	}

	@Override
	public List<SearchEntityKeyword> getKeywords(PostDeleteEvent event) {
		return getKeywords(event.getEntity(), event.getPersister(), event.getDeletedState(), null);
	}

	public abstract Set<Long> getEntityIds(Object entity);

	public abstract List<String> getKeywordProps();

	public abstract String getEntityName();

	public abstract boolean isEntityDeleted(Object entity);

	protected List<SearchEntityKeyword> getKeywords(Object entity, EntityPersister persister, Object[] oldState, Object[] newState) {
		// op: 0: insert, 1: update, 2: delete
		int op = oldState != null && newState != null ? 1 : (oldState != null ? 2 : (newState != null ? 0 : -1));
		Set<Long> entityIds = getEntityIds(entity);

		List<SearchEntityKeyword> keywords = new ArrayList<>();
		if (op == 2) {
			for (String prop : getKeywordProps()) {
				keywords.addAll(createKeywords(entityIds, prop, getProperty(prop, persister, oldState), null, op));
			}
		} else {
			boolean entityDeleted = isEntityDeleted(entity);
			for (String prop : getKeywordProps()) {
				Pair<String, String> values = getProperty(prop, persister, oldState, newState);
				if (entityDeleted || !StringUtils.equals(values.first(), values.second())) {
					keywords.addAll(createKeywords(entityIds, prop, values.first(), values.second(), op));
				}
			}

			if (entityDeleted) {
				keywords.forEach(keyword -> keyword.setStatus(0));
			}
		}

		return keywords;
	}

	protected List<SearchEntityKeyword> createKeywords(Set<Long> entityIds, String property, String oldValue, String value, int op) {
		return entityIds.stream().map(entityId -> {
			SearchEntityKeyword keyword = new SearchEntityKeyword();
			keyword.setEntity(getEntityName());
			keyword.setEntityId(entityId);
			keyword.setKey(property);
			keyword.setValue(value);
			keyword.setStatus(1);
			keyword.setOp(op);
			keyword.setOldValue(oldValue);
			return keyword;
		}).collect(Collectors.toList());
	}

	protected Object getObject(String propertyName, EntityPersister persister, Object[] state) {
		int propIdx = getPropertyIndex(persister, propertyName);
		return getObject(state, propIdx);
	}

	private Pair<String, String> getProperty(String propertyName, EntityPersister persister, Object[] oldState, Object[] newState) {
		int propertyIdx = getPropertyIndex(persister, propertyName);
		String oldValue = getValue(oldState, propertyIdx);
		String newValue = getValue(newState, propertyIdx);
		return Pair.make(oldValue, newValue);
	}

	private String getProperty(String propertyName, EntityPersister persister, Object[] state) {
		int propertyIdx = getPropertyIndex(persister, propertyName);
		return getValue(state, propertyIdx);
	}

	private int getPropertyIndex(EntityPersister persister, String propertyName) {
		String[] propertyNames = persister.getPropertyNames();
		for (int i = 0; i < propertyNames.length; ++i) {
			if (propertyNames[i].equals(propertyName)) {
				return i;
			}
		}

		throw new IllegalArgumentException("Invalid property " + propertyName + " for " + persister.getEntityName());
	}

	private Object getObject(Object[] state, int propertyIdx) {
		if (state == null) {
			return null;
		}

		if (propertyIdx >= state.length) {
			throw new IllegalArgumentException(
				"Invalid object state. Attempting to read property " + (propertyIdx + 1) +
					" when the object has only " + state.length + " properties"
			);
		}

		return state[propertyIdx];
	}

	private String getValue(Object[] state, int propertyIdx) {
		Object value = getObject(state, propertyIdx);
		return value != null ? value.toString() : null;
	}
}
