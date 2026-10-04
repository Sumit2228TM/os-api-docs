package com.krishagni.catissueplus.rest.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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

import com.krishagni.catissueplus.core.administrative.events.ContainerTaskDetail;
import com.krishagni.catissueplus.core.administrative.repository.ContainerTaskListCriteria;
import com.krishagni.catissueplus.core.administrative.services.ContainerMaintenanceService;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/container-tasks")
public class ContainerTaskController {
	@Autowired
	private ContainerMaintenanceService containerMaintSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ContainerTaskDetail> getTasks(
		@RequestParam(value = "name", required = false)
		String name,

		@RequestParam(value = "activityStatus", required = false, defaultValue = "Active")
		String activityStatus,

		@RequestParam(value = "startAt", defaultValue = "0")
		int startAt,

		@RequestParam(value = "maxResults", defaultValue = "100")
		int maxResults) {

		ContainerTaskListCriteria crit = new ContainerTaskListCriteria()
			.query(name).activityStatus(activityStatus).startAt(startAt).maxResults(maxResults);
		return ResponseEvent.unwrap(containerMaintSvc.getTasks(RequestEvent.wrap(crit)));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/count")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Integer> getTasks(
		@RequestParam(value = "name", required = false)
		String name,

		@RequestParam(value = "activityStatus", required = false, defaultValue = "Active")
		String activityStatus) {

		ContainerTaskListCriteria crit = new ContainerTaskListCriteria().query(name).activityStatus(activityStatus);
		Integer count = ResponseEvent.unwrap(containerMaintSvc.getTasksCount(RequestEvent.wrap(crit)));
		return Collections.singletonMap("count", count);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ContainerTaskDetail getTask(@PathVariable("id") Long taskId) {
		return ResponseEvent.unwrap(containerMaintSvc.getTask(RequestEvent.wrap(new EntityQueryCriteria(taskId))));
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ContainerTaskDetail createTask(@RequestBody ContainerTaskDetail input) {
		return ResponseEvent.unwrap(containerMaintSvc.createTask(RequestEvent.wrap(input)));
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ContainerTaskDetail updateTask(@PathVariable("id") Long taskId, @RequestBody ContainerTaskDetail input) {
		input.setId(taskId);
		return ResponseEvent.unwrap(containerMaintSvc.updateTask(RequestEvent.wrap(input)));
	}
}
