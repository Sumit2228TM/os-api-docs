package com.krishagni.catissueplus.core.common.service;

public interface ObjectAccessorFactory {
	ObjectAccessor getAccessor(String objectName);

	void addAccessor(ObjectAccessor accessor);
}
