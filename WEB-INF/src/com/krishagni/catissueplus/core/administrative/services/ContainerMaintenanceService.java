package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.ContainerActivityLogDetail;
import com.krishagni.catissueplus.core.administrative.events.ContainerTaskDetail;
import com.krishagni.catissueplus.core.administrative.events.ScheduledContainerActivityDetail;
import com.krishagni.catissueplus.core.administrative.repository.ContainerActivityLogListCriteria;
import com.krishagni.catissueplus.core.administrative.repository.ContainerTaskListCriteria;
import com.krishagni.catissueplus.core.administrative.repository.ScheduledContainerActivityListCriteria;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface ContainerMaintenanceService {

	//
	// tasks
	//
	ResponseEvent<List<ContainerTaskDetail>> getTasks(RequestEvent<ContainerTaskListCriteria> req);

	ResponseEvent<Integer> getTasksCount(RequestEvent<ContainerTaskListCriteria> req);

	ResponseEvent<ContainerTaskDetail> getTask(RequestEvent<EntityQueryCriteria> req);

	ResponseEvent<ContainerTaskDetail> createTask(RequestEvent<ContainerTaskDetail> req);

	ResponseEvent<ContainerTaskDetail> updateTask(RequestEvent<ContainerTaskDetail> req);

	//
	// scheduled activities
	//
	ResponseEvent<List<ScheduledContainerActivityDetail>> getScheduledActivities(RequestEvent<ScheduledContainerActivityListCriteria> req);

	ResponseEvent<ScheduledContainerActivityDetail> getScheduledActivity(RequestEvent<EntityQueryCriteria> req);

	ResponseEvent<ScheduledContainerActivityDetail> createScheduledActivity(RequestEvent<ScheduledContainerActivityDetail> req);

	ResponseEvent<ScheduledContainerActivityDetail> updateScheduledActivity(RequestEvent<ScheduledContainerActivityDetail> req);

	//
	// activity logs
	//
	ResponseEvent<List<ContainerActivityLogDetail>> getActivityLogs(RequestEvent<ContainerActivityLogListCriteria> req);

	ResponseEvent<ContainerActivityLogDetail> getActivityLog(RequestEvent<EntityQueryCriteria> req);

	ResponseEvent<ContainerActivityLogDetail> createActivityLog(RequestEvent<ContainerActivityLogDetail> req);

	ResponseEvent<ContainerActivityLogDetail> updateActivityLog(RequestEvent<ContainerActivityLogDetail> req);
}
