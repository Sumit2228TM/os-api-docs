package com.krishagni.catissueplus.core.de.repository;

public interface DaoFactory {
	FormDao getFormDao();
	
	SavedQueryDao getSavedQueryDao();
	
	QueryFolderDao getQueryFolderDao();
	
	QueryAuditLogDao getQueryAuditLogDao();
}
