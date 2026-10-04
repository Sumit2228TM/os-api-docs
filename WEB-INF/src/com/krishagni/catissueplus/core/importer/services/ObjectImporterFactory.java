package com.krishagni.catissueplus.core.importer.services;

public interface ObjectImporterFactory {
	<T, U> ObjectImporter<T, U> getImporter(String objectType);
	
	<T, U> void registerImporter(String objectType, ObjectImporter<T, U> importer);
}
