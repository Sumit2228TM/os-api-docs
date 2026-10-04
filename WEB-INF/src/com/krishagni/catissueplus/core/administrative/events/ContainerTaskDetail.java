package com.krishagni.catissueplus.core.administrative.events;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.administrative.domain.ContainerTask;
import com.krishagni.catissueplus.core.common.util.Utility;

public class ContainerTaskDetail {
	private Long id;

	private String name;

	private String description;

	private String activityStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public static ContainerTaskDetail from(ContainerTask task) {
		ContainerTaskDetail result = new ContainerTaskDetail();
		result.setId(task.getId());
		result.setName(task.getName());
		result.setDescription(task.getDescription());
		result.setActivityStatus(task.getActivityStatus());
		return result;
	}

	public static List<ContainerTaskDetail> from(Collection<ContainerTask> tasks) {
		return Utility.nullSafeStream(tasks).map(ContainerTaskDetail::from).collect(Collectors.toList());
	}
}
