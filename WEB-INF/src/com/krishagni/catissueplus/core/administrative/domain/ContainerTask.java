package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Calendar;

import org.hibernate.envers.Audited;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.common.util.Status;

@Audited
public class ContainerTask extends BaseEntity {
	private String name;

	private String description;

	private String activityStatus;

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

	public void update(ContainerTask other) {
		setName(other.getName());
		setDescription(other.getDescription());
		setActivityStatus(other.getActivityStatus());

		if (Status.ACTIVITY_STATUS_DISABLED.getStatus().equals(getActivityStatus())) {
			setName(getName() + "_" + Calendar.getInstance().getTimeInMillis());
		}
	}
}
