package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.StagedVisit;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface StagedVisitDao extends Dao<StagedVisit> {
	StagedVisit getByName(String name);

	StagedVisit getBySprNo(String sprNo);

	List<StagedVisit> getByEmpiOrMrn(String empiOrMrn);

	int deleteOldVisits(int olderThanDays);
}
