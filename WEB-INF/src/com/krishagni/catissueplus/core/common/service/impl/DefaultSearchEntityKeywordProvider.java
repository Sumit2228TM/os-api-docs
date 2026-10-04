package com.krishagni.catissueplus.core.common.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

public class DefaultSearchEntityKeywordProvider extends AbstractSearchEntityKeywordProvider {
	private Class<?> entityClass;

	private String entityName;

	private List<String> props;

	private Function<Object, Boolean> isEntityDeletedFn;

	public DefaultSearchEntityKeywordProvider() {
	}

	public DefaultSearchEntityKeywordProvider(Class<?> entityClass, String entityName, String props) {
		this(entityClass, entityName, props, null);
	}

	public DefaultSearchEntityKeywordProvider(Class<?> entityClass, String entityName, String props, Function<Object, Boolean> isEntityDeletedFn) {
		this.entityClass = entityClass;
		this.entityName = entityName;
		this.props = Utility.csvToStringList(props);
		this.isEntityDeletedFn = isEntityDeletedFn;
	}

	public DefaultSearchEntityKeywordProvider entityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
		return this;
	}

	public DefaultSearchEntityKeywordProvider entityName(String entityName) {
		this.entityName = entityName;
		return this;
	}

	public DefaultSearchEntityKeywordProvider keywordProps(String props) {
		this.props = Utility.csvToStringList(props);
		return this;
	}

	@Override
	public String getEntity() {
		return entityClass.getName();
	}

	@Override
	public Set<Long> getEntityIds(Object entity) {
		if (entity instanceof BaseEntity) {
			return Collections.singleton(((BaseEntity) entity).getId());
		} else {
			return Collections.emptySet();
		}
	}

	@Override
	public String getEntityName() {
		return entityName;
	}

	@Override
	public List<String> getKeywordProps() {
		return props;
	}

	@Override
	public boolean isEntityDeleted(Object entity) {
		if (isEntityDeletedFn != null) {
			return isEntityDeletedFn.apply(entity);
		}

		BeanWrapper bean = PropertyAccessorFactory.forBeanPropertyAccess(entity);
		String activityStatus = (String) bean.getPropertyValue("activityStatus");
		return Status.ACTIVITY_STATUS_DISABLED.equals(activityStatus);
	}
}
