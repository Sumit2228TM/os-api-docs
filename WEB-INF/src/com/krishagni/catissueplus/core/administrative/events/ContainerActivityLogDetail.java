package com.krishagni.catissueplus.core.administrative.events;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.administrative.domain.ContainerActivityLog;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.Utility;

public class ContainerActivityLogDetail {
	private Long id;

	private Long containerId;

	private String containerName;

	private Long scheduledActivityId;

	private String scheduledActivityName;

	private Long taskId;

	private String taskName;

	private UserSummary performedBy;

	private Date activityDate;

	private Integer timeTaken;

	private String comments;

	private String activityStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getContainerId() {
		return containerId;
	}

	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public Long getScheduledActivityId() {
		return scheduledActivityId;
	}

	public void setScheduledActivityId(Long scheduledActivityId) {
		this.scheduledActivityId = scheduledActivityId;
	}

	public String getScheduledActivityName() {
		return scheduledActivityName;
	}

	public void setScheduledActivityName(String scheduledActivityName) {
		this.scheduledActivityName = scheduledActivityName;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public UserSummary getPerformedBy() {
		return performedBy;
	}

	public void setPerformedBy(UserSummary performedBy) {
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

	public static ContainerActivityLogDetail from(ContainerActivityLog log) {
		ContainerActivityLogDetail result = new ContainerActivityLogDetail();
		result.setId(log.getId());
		result.setContainerId(log.getContainer().getId());
		result.setContainerName(log.getContainer().getName());

		if (log.getActivity() != null) {
			result.setScheduledActivityId(log.getActivity().getId());
			result.setScheduledActivityName(log.getActivity().getName());
		}

		result.setTaskId(log.getTask().getId());
		result.setTaskName(log.getTask().getName());
		result.setPerformedBy(UserSummary.from(log.getPerformedBy()));
		result.setActivityDate(log.getActivityDate());
		result.setTimeTaken(log.getTimeTaken());
		result.setComments(log.getComments());
		result.setActivityStatus(log.getActivityStatus());
		return result;
	}

	public static List<ContainerActivityLogDetail> from(Collection<ContainerActivityLog> logs) {
		return Utility.nullSafeStream(logs).map(ContainerActivityLogDetail::from).collect(Collectors.toList());
	}
}
