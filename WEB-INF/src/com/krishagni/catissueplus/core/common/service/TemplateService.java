package com.krishagni.catissueplus.core.common.service;

import java.io.File;
import java.util.Map;

public interface TemplateService {
	String render(String templateName, Map<String, Object> properties);

	void render(String templateName, Map<String, Object> properties, File output);
}
