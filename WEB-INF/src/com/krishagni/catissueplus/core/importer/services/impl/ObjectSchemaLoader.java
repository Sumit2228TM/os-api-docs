package com.krishagni.catissueplus.core.importer.services.impl;

import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.importer.services.ObjectSchemaBuilder;
import com.krishagni.catissueplus.core.importer.services.ObjectSchemaFactory;


public class ObjectSchemaLoader {
	private ObjectSchemaFactory objectSchemaFactory;
	
	public void setObjectSchemaFactory(ObjectSchemaFactory objectSchemaFactory) {
		this.objectSchemaFactory = objectSchemaFactory;
	}
	
	public void setSchemaResources(List<String> schemaResources) {
		for (String schemaResource : schemaResources) {
			objectSchemaFactory.registerSchema(schemaResource);
		}
	}

	public void setSchemaBuilders(Map<String, ObjectSchemaBuilder> builders) {
		builders.forEach((schemaName, builder) -> objectSchemaFactory.registerSchemaBuilder(schemaName, builder));
	}
}
