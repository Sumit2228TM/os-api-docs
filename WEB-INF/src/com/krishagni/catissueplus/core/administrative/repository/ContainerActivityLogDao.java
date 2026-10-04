package com.krishagni.catissueplus.core.administrative.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.ContainerActivityLog;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface ContainerActivityLogDao extends Dao<ContainerActivityLog> {
	List<ContainerActivityLog> getActivityLogs(ContainerActivityLogListCriteria crit);

	Map<Long, Date> getLatestScheduledActivityDate(Collection<Long> schedActivityIds);
}
