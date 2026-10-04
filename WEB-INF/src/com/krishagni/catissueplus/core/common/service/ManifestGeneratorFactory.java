package com.krishagni.catissueplus.core.common.service;

public interface ManifestGeneratorFactory {
	<T> ManifestGenerator<T> getGenerator(String type);

	<T> void addGenerator(ManifestGenerator<T> generator);
}
