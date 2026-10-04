package com.krishagni.catissueplus.core.exporter.services;

import java.util.List;

@FunctionalInterface
public interface ObjectsGenerator {
	List<Object> apply(int startAt, int maxResults);
}
