package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;

public class StagedConsentDetail {
	private String type;

	private String status;

	private Date decisionDateTime;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDecisionDateTime() {
		return decisionDateTime;
	}

	public void setDecisionDateTime(Date decisionDateTime) {
		this.decisionDateTime = decisionDateTime;
	}
}
