package com.krishagni.catissueplus.core.common.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class PrintRuleConfigsListCriteria extends AbstractListCriteria<PrintRuleConfigsListCriteria> {
	private String objectType;

	private String instituteName;

	private String userName;

	@Override
	public PrintRuleConfigsListCriteria self() {
		return this;
	}

	public String objectType() {
		return objectType;
	}

	public PrintRuleConfigsListCriteria objectType(String objectType) {
		this.objectType = objectType;
		return self();
	}

	public String instituteName() {
		return instituteName;
	}

	public PrintRuleConfigsListCriteria instituteName(String instituteName) {
		this.instituteName = instituteName;
		return self();
	}

	public String userName() {
		return userName;
	}

	public PrintRuleConfigsListCriteria userName(String userName) {
		this.userName = userName;
		return self();
	}
}
