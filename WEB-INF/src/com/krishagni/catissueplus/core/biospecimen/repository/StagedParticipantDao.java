package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.StagedParticipant;
import com.krishagni.catissueplus.core.biospecimen.events.PmiDetail;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface StagedParticipantDao extends Dao<StagedParticipant> {

	List<StagedParticipant> getByPmis(List<PmiDetail> pmis);
	
	StagedParticipant getByEmpi(String empi);

	StagedParticipant getByUid(String uid);

	List<StagedParticipant> getByMrn(String mrn);

	int deleteOldParticipants(int olderThanDays);
}
