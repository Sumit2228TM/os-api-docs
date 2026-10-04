package com.krishagni.catissueplus.core.common.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.common.service.ManifestGenerator;
import com.krishagni.catissueplus.core.common.service.ManifestGeneratorFactory;

public class ManifestGeneratorFactoryImpl implements ManifestGeneratorFactory {
	private Map<String, ManifestGenerator> generators = new HashMap<>();

	@Override
	public <T> ManifestGenerator<T> getGenerator(String type) {
		return generators.get(type);
	}

	@Override
	public <T> void addGenerator(ManifestGenerator<T> generator) {
		generators.put(generator.getType(), generator);
	}
}
