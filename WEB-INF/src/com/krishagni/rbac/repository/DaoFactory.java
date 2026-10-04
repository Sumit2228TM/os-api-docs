package com.krishagni.rbac.repository;

import com.krishagni.catissueplus.core.administrative.repository.SiteDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.biospecimen.repository.ParticipantDao;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenDao;
import com.krishagni.catissueplus.core.biospecimen.repository.VisitsDao;

public interface DaoFactory {
	ResourceDao getResourceDao();
	
	OperationDao getOperationDao();
	
	PermissionDao getPermissionDao();
	
	RoleDao getRoleDao();
	
	GroupDao getGroupDao();
	
	SubjectDao getSubjectDao();
		
	CollectionProtocolDao getCollectionProtocolDao();
	
	SiteDao getSiteDao();
	
	CollectionProtocolRegistrationDao getCprDao();

	ParticipantDao getParticipantDao();
	
	VisitsDao getVisitDao();
	
	SpecimenDao getSpecimenDao();
}
