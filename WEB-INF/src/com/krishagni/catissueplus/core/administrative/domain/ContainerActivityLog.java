package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Date;

import org.hibernate.envers.Audited;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

@Audited
public class ContainerActivityLog extends BaseEntity {
	private StorageContainer container;

	private ScheduledContainerActivity activity;

	private ContainerTask task;

	private User performedBy;

	private Date activityDate;

	private Integer timeTaken;

	private String comments;

	private String activityStatus;

	public StorageContainer getContainer() {
		return container;
	}

	public void setContainer(StorageContainer container) {
		this.container = container;
	}

	public ScheduledContainerActivity getActivity() {
		return activity;
	}

	public void setActivity(ScheduledContainerActivity activity) {
		this.activity = activity;
	}

	public ContainerTask getTask() {
		return task;
	}

	public void setTask(ContainerTask task) {
		this.task = task;
	}

	public User getPerformedBy() {
		return performedBy;
	}

	public void setPerformedBy(User performedBy) {
		this.performedBy = performedBy;
	}

	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	public Integer getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(Integer timeTaken) {
		this.timeTaken = timeTaken;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public void update(ContainerActivityLog other) {
		setContainer(other.getContainer());
		setActivity(other.getActivity());
		setTask(other.getTask());
		setPerformedBy(other.getPerformedBy());
		setActivityDate(other.getActivityDate());
		setTimeTaken(other.getTimeTaken());
		setComments(other.getComments());
		setActivityStatus(other.getActivityStatus());
	}
}
