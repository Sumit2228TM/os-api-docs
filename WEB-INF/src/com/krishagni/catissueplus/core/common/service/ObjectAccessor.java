package com.krishagni.catissueplus.core.common.service;

import java.util.Map;

public interface ObjectAccessor {
	String getObjectName();

	Map<String, Object> resolveUrl(String key, Object value);

	String getAuditTable();

	void ensureReadAllowed(Long objectId);

	//
	// later: getObject(Map<String, Object> ids);
	//
}
