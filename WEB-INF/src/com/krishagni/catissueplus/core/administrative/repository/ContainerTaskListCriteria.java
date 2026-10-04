package com.krishagni.catissueplus.core.administrative.repository;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ContainerTaskListCriteria extends AbstractListCriteria<ContainerTaskListCriteria> {
	private String activityStatus;

	@Override
	public ContainerTaskListCriteria self() {
		return this;
	}

	public String activityStatus() {
		return activityStatus;
	}

	public ContainerTaskListCriteria activityStatus(String activityStatus) {
		if (StringUtils.equalsIgnoreCase(activityStatus, "all")) {
			activityStatus = null;
		} else if (StringUtils.equalsIgnoreCase(activityStatus, "archived")) {
			activityStatus = "Closed";
		}

		this.activityStatus = activityStatus;
		return self();
	}
}
