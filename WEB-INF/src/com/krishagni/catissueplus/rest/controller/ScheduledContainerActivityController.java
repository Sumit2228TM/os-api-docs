package com.krishagni.catissueplus.rest.controller;

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

import com.krishagni.catissueplus.core.administrative.events.ScheduledContainerActivityDetail;
import com.krishagni.catissueplus.core.administrative.repository.ScheduledContainerActivityListCriteria;
import com.krishagni.catissueplus.core.administrative.services.ContainerMaintenanceService;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/scheduled-container-activities")
public class ScheduledContainerActivityController {

	@Autowired
	private ContainerMaintenanceService containerMaintSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ScheduledContainerActivityDetail> getActivities(
		@RequestParam(value = "containerId")
		Long containerId,

		@RequestParam(value = "taskId", required = false)
		Long taskId,

		@RequestParam(value = "taskName", required = false)
		String taskName,

		@RequestParam(value = "activityStatus", required = false, defaultValue = "Active")
		String activityStatus,

		@RequestParam(value = "startAt", defaultValue = "0")
		int startAt,

		@RequestParam(value = "maxResults", defaultValue = "100")
		int maxResults) {

		ScheduledContainerActivityListCriteria crit = new ScheduledContainerActivityListCriteria()
			.containerId(containerId)
			.taskId(taskId)
			.taskName(taskName)
			.activityStatus(activityStatus)
			.startAt(startAt)
			.maxResults(maxResults);
		return ResponseEvent.unwrap(containerMaintSvc.getScheduledActivities(RequestEvent.wrap(crit)));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScheduledContainerActivityDetail getActivity(@PathVariable("id") Long activityId) {
		EntityQueryCriteria crit = new EntityQueryCriteria(activityId);
		return ResponseEvent.unwrap(containerMaintSvc.getScheduledActivity(RequestEvent.wrap(crit)));
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScheduledContainerActivityDetail createActivity(@RequestBody ScheduledContainerActivityDetail input) {
		return ResponseEvent.unwrap(containerMaintSvc.createScheduledActivity(RequestEvent.wrap(input)));
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScheduledContainerActivityDetail updateActivity(
		@PathVariable("id")
		Long activityId,

		@RequestBody
		ScheduledContainerActivityDetail input) {
		input.setId(activityId);
		return ResponseEvent.unwrap(containerMaintSvc.updateScheduledActivity(RequestEvent.wrap(input)));
	}
}
