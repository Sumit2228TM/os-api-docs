package com.krishagni.catissueplus.core.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;

import org.apache.commons.lang3.StringUtils;

public class PluginManager {
	private static final PluginManager instance = new PluginManager();

	private Map<String, Map<String, String>> plugins = new LinkedHashMap<>();

	public static PluginManager getInstance() {
		return instance;
	}
	
	public List<String> getPluginNames() {
		return new ArrayList<>(plugins.keySet());
	}

	public List<Map<String, String>> getPlugins() {
		return new ArrayList<>(plugins.values());
	}
	
	public boolean addPlugin(Attributes attrs) {
		String[] attrNames = {"os-plugin-name", "built-on", "commit", "version"};
		String name = attrs.getValue("os-plugin-name");

		Map<String, String> props = new LinkedHashMap<>();
		for (String attrName : attrNames) {
			String value = attrs.getValue(attrName);
			if (StringUtils.isBlank(value)) {
				value = "unknown";
			}

			props.put(attrName, value);
		}

		plugins.put(name, props);
		return true;
	}
}
