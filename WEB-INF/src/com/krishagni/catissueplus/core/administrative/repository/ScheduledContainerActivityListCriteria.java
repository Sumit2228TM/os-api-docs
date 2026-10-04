package com.krishagni.catissueplus.core.administrative.repository;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ScheduledContainerActivityListCriteria extends AbstractListCriteria<ScheduledContainerActivityListCriteria> {
	private Long containerId;

	private Long taskId;

	private String taskName;

	private String activityStatus;

	@Override
	public ScheduledContainerActivityListCriteria self() {
		return this;
	}

	public Long containerId() {
		return containerId;
	}

	public ScheduledContainerActivityListCriteria containerId(Long containerId) {
		this.containerId = containerId;
		return self();
	}

	public Long taskId() {
		return taskId;
	}

	public ScheduledContainerActivityListCriteria taskId(Long taskId) {
		this.taskId = taskId;
		return self();
	}

	public String taskName() {
		return taskName;
	}

	public ScheduledContainerActivityListCriteria taskName(String taskName) {
		this.taskName = taskName;
		return self();
	}

	public String activityStatus() {
		return activityStatus;
	}

	public ScheduledContainerActivityListCriteria activityStatus(String activityStatus) {
		if (StringUtils.equalsIgnoreCase(activityStatus, "all")) {
			activityStatus = null;
		} else if (StringUtils.equalsIgnoreCase(activityStatus, "archived")) {
			activityStatus = "Closed";
		}

		this.activityStatus = activityStatus;
		return self();
	}
}
