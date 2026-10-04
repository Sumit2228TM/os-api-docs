package com.krishagni.catissueplus.core.common.events;

import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.common.domain.MessageLog;

public class MessageLogCriteria extends AbstractListCriteria<MessageLogCriteria> {
	private String externalApp;

	private List<String> msgTypes;

	private Integer maxRetries;

	private Date fromDate;

	private Date toDate;

	private MessageLog.ProcessStatus status;

	@Override
	public MessageLogCriteria self() {
		return this;
	}

	public String externalApp() {
		return externalApp;
	}

	public MessageLogCriteria externalApp(String externalApp) {
		this.externalApp = externalApp;
		return self();
	}

	public List<String> msgTypes() {
		return msgTypes;
	}

	public MessageLogCriteria msgTypes(List<String> msgTypes) {
		this.msgTypes = msgTypes;
		return self();
	}

	public Integer maxRetries() {
		return maxRetries;
	}

	public MessageLogCriteria maxRetries(Integer maxRetries) {
		this.maxRetries = maxRetries;
		return self();
	}

	public Date fromDate() {
		return fromDate;
	}

	public MessageLogCriteria fromDate(Date fromDate) {
		this.fromDate = fromDate;
		return self();
	}

	public Date toDate() {
		return toDate;
	}

	public MessageLogCriteria toDate(Date toDate) {
		this.toDate = toDate;
		return self();
	}

	public MessageLog.ProcessStatus processStatus() {
		return status;
	}

	public MessageLogCriteria processStatus(MessageLog.ProcessStatus status) {
		this.status = status;
		return self();
	}
}
