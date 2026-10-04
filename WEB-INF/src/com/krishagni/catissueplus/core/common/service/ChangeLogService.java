package com.krishagni.catissueplus.core.common.service;

public interface ChangeLogService {
	boolean doesChangeLogExists(String id, String author, String filename);

	void insertChangeLog(String id, String author, String filename);
}
