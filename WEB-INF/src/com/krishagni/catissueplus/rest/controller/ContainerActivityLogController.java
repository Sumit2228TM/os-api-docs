package com.krishagni.catissueplus.rest.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.administrative.events.ContainerActivityLogDetail;
import com.krishagni.catissueplus.core.administrative.repository.ContainerActivityLogListCriteria;
import com.krishagni.catissueplus.core.administrative.services.ContainerMaintenanceService;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/container-activity-logs")
public class ContainerActivityLogController {
	@Autowired
	private ContainerMaintenanceService containerMaintSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ContainerActivityLogDetail> getActivityLogs(
		@RequestParam(value = "containerId")
		Long containerId,

		@RequestParam(value = "fromDate", required = false)
		Date fromDate,

		@RequestParam(value = "toDate", required = false)
		Date toDate,

		@RequestParam(value = "performedBy", required = false)
		Long performedBy,

		@RequestParam(value = "taskId", required = false)
		Long taskId,

		@RequestParam(value = "scheduledActivityId", required = false)
		Long scheduledActivityId,

		@RequestParam(value = "startAt", required = false, defaultValue = "0")
		int startAt,

		@RequestParam(value = "maxResults", required = false, defaultValue = "100")
		int maxResults) {

		ContainerActivityLogListCriteria crit = new ContainerActivityLogListCriteria()
			.containerId(containerId)
			.fromDate(fromDate)
			.toDate(toDate)
			.performedBy(performedBy)
			.taskId(taskId)
			.scheduledActivityId(scheduledActivityId)
			.startAt(startAt)
			.maxResults(maxResults);
		return ResponseEvent.unwrap(containerMaintSvc.getActivityLogs(RequestEvent.wrap(crit)));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ContainerActivityLogDetail getActivityLog(@PathVariable("id") Long activityId) {
		return ResponseEvent.unwrap(containerMaintSvc.getActivityLog(RequestEvent.wrap(new EntityQueryCriteria(activityId))));
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ContainerActivityLogDetail createActivityLog(@RequestBody ContainerActivityLogDetail input) {
		return ResponseEvent.unwrap(containerMaintSvc.createActivityLog(RequestEvent.wrap(input)));
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ContainerActivityLogDetail updateActivityLog(
		@PathVariable("id")
		Long activityId,

		@RequestBody
		ContainerActivityLogDetail input) {

		input.setId(activityId);
		return ResponseEvent.unwrap(containerMaintSvc.updateActivityLog(RequestEvent.wrap(input)));
	}
}
