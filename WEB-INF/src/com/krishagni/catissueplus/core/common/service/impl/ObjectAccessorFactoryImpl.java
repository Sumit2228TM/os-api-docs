package com.krishagni.catissueplus.core.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.common.service.ObjectAccessor;
import com.krishagni.catissueplus.core.common.service.ObjectAccessorFactory;

public class ObjectAccessorFactoryImpl implements ObjectAccessorFactory {
	private Map<String, ObjectAccessor> accessors = new HashMap<>();

	@Override
	public ObjectAccessor getAccessor(String objectName) {
		return accessors.get(objectName);
	}

	@Override
	public void addAccessor(ObjectAccessor accessor) {
		accessors.put(accessor.getObjectName(), accessor);
	}

	public void setAccessors(List<ObjectAccessor> accessors) {
		accessors.forEach(accessor -> this.accessors.put(accessor.getObjectName(), accessor));
	}
}
