package com.krishagni.catissueplus.core.exporter.events;

import java.util.Date;
import java.util.Map;

import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.exporter.domain.ExportJob;

public class ExportJobDetail {
	private Long id;

	private String name;

	private String status;

	private Long totalRecords;

	private UserSummary createdBy;

	private Date creationTime;

	private Date endTime;

	private Map<String, String> params;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public UserSummary getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserSummary createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public static ExportJobDetail from(ExportJob job) {
		ExportJobDetail detail = new ExportJobDetail();
		detail.setId(job.getId());
		detail.setName(job.getName());
		detail.setStatus(job.getStatus().name());
		detail.setCreatedBy(UserSummary.from(job.getCreatedBy()));
		detail.setCreationTime(job.getCreationTime());
		detail.setEndTime(job.getEndTime());
		detail.setParams(job.getParams());
		return detail;
	}
}
