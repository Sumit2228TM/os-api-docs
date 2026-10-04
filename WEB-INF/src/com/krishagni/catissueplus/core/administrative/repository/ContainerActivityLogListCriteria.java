package com.krishagni.catissueplus.core.administrative.repository;

import java.util.Date;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ContainerActivityLogListCriteria extends AbstractListCriteria<ContainerActivityLogListCriteria> {
	private Date fromDate;

	private Date toDate;

	private Long performedBy;

	private Long containerId;

	private Long taskId;

	private Long scheduledActivityId;

	@Override
	public ContainerActivityLogListCriteria self() {
		return this;
	}

	public Date fromDate() {
		return fromDate;
	}

	public ContainerActivityLogListCriteria fromDate(Date fromDate) {
		this.fromDate = fromDate;
		return self();
	}

	public Date toDate() {
		return toDate;
	}

	public ContainerActivityLogListCriteria toDate(Date toDate) {
		this.toDate = toDate;
		return self();
	}

	public Long performedBy() {
		return performedBy;
	}

	public ContainerActivityLogListCriteria performedBy(Long performedBy) {
		this.performedBy = performedBy;
		return self();
	}

	public Long containerId() {
		return containerId;
	}

	public ContainerActivityLogListCriteria containerId(Long containerId) {
		this.containerId = containerId;
		return self();
	}

	public Long taskId() {
		return taskId;
	}

	public ContainerActivityLogListCriteria taskId(Long taskId) {
		this.taskId = taskId;
		return self();
	}

	public Long scheduledActivityId() {
		return scheduledActivityId;
	}

	public ContainerActivityLogListCriteria scheduledActivityId(Long scheduledActivityId) {
		this.scheduledActivityId = scheduledActivityId;
		return self();
	}
}
