package com.krishagni.catissueplus.core.common.domain;

import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class MessageLog extends BaseEntity {
	public enum Status {
		SUCCESS,
		FAIL
	}

	public enum ProcessStatus {
		PROCESSED,
		PENDING
	}

	private String externalApp;

	private String type;

	private String message;

	private Date receiveTime;

	private String recordId;

	private Status status;

	private ProcessStatus processStatus;

	private Date processTime;

	private Integer noOfRetries;

	private String error;

	public String getExternalApp() {
		return externalApp;
	}

	public void setExternalApp(String externalApp) {
		this.externalApp = externalApp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public ProcessStatus getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(ProcessStatus processStatus) {
		this.processStatus = processStatus;
	}

	public Date getProcessTime() {
		return processTime;
	}

	public void setProcessTime(Date processTime) {
		this.processTime = processTime;
	}

	public Integer getNoOfRetries() {
		return noOfRetries;
	}

	public void setNoOfRetries(Integer noOfRetries) {
		this.noOfRetries = noOfRetries;
	}

	public int incrNoOfRetries() {
		return noOfRetries == null ? (noOfRetries = 1) : ++noOfRetries;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
