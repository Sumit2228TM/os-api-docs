package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.ContainerTask;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface ContainerTaskDao extends Dao<ContainerTask> {

	List<ContainerTask> getTasks(ContainerTaskListCriteria crit);

	Integer getTasksCount(ContainerTaskListCriteria crit);

	ContainerTask getByName(String name);
}
