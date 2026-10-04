package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.biospecimen.domain.ConsentStatement;

public class ConsentStatementDetail {
	private Long id;
	
	private String code;
	
	private String statement;
	
	private String activityStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
	
	public static ConsentStatementDetail from(ConsentStatement consent) {
		ConsentStatementDetail detail = new ConsentStatementDetail();
		detail.setId(consent.getId());
		detail.setCode(consent.getCode());
		detail.setStatement(consent.getStatement());
		detail.setActivityStatus(consent.getActivityStatus());
		return detail;
	}

	public static List<ConsentStatementDetail> from(Collection<ConsentStatement> consents) {
		return consents.stream().map(ConsentStatementDetail::from).collect(Collectors.toList());
	}
}
