package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.repository.Dao;


public interface InstituteDao extends Dao<Institute> {
	List<InstituteDetail> getInstitutes(InstituteListCriteria listCrit);
	
	Long getInstitutesCount(InstituteListCriteria listCrit);

	List<Institute> getInstituteByNames(List<String> names);

	Institute getInstituteByName(String name);

	List<DependentEntityDetail> getDependentEntities(Long instituteId);
}
