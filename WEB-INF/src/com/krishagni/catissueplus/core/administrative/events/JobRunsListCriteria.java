package com.krishagni.catissueplus.core.administrative.events;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class JobRunsListCriteria extends AbstractListCriteria<JobRunsListCriteria> {
	private Long jobId;

	private Date fromDate;

	private Date toDate;

	private ScheduledJobRun.Status status;

	private Long userId;

	@Override
	public JobRunsListCriteria self() {
		return this;
	}

	@JsonProperty("jobId")
	public Long jobId() {
		return jobId;
	}
	
	public JobRunsListCriteria jobId(Long jobId) {
		this.jobId = jobId;
		return self();
	}

	@JsonProperty("startDate")
	public Date fromDate() {
		return fromDate;
	}

	public JobRunsListCriteria fromDate(Date fromDate) {
		this.fromDate = fromDate;
		return self();
	}

	@JsonProperty("endDate")
	public Date toDate() {
		return toDate;
	}

	public JobRunsListCriteria toDate(Date toDate) {
		this.toDate = toDate;
		return self();
	}

	@JsonProperty("status")
	public ScheduledJobRun.Status status() {
		return status;
	}

	public JobRunsListCriteria status(ScheduledJobRun.Status status) {
		this.status = status;
		return self();
	}

	public Long userId() {
		return userId;
	}

	public JobRunsListCriteria userId(Long userId) {
		this.userId = userId;
		return self();
	}
}
