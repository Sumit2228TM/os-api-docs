package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledContainerActivity;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface ScheduledContainerActivityDao extends Dao<ScheduledContainerActivity> {
	List<ScheduledContainerActivity> getActivities(ScheduledContainerActivityListCriteria crit);

	ScheduledContainerActivity getActivity(Long containerId, String name);
}
