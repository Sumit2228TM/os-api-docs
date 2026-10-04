package com.krishagni.catissueplus.core.common.service;

import java.io.File;
import java.util.Map;

public interface ManifestGenerator<T> {
	String getType();

	File generateManifest(Map<String, Object> input);

	File generateManifest(T input);
}
