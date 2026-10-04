package com.krishagni.catissueplus.core.importer.services;

import java.util.Map;

public interface ObjectImporterLifecycle {
	void start(String id);

	void stop(String id, Map<String, Object> runCtxt);
}
