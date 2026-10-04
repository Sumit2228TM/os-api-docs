package com.krishagni.catissueplus.core.biospecimen.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ConsentStatementListCriteria extends AbstractListCriteria<ConsentStatementListCriteria> {
	private String code;

	private String statement;
	
	@Override
	public ConsentStatementListCriteria self() {
		return this;
	}

	public String code() {
		return code;
	}

	public ConsentStatementListCriteria code(String code) {
		this.code = code;
		return self();
	}

	public String statement() {
		return statement;
	}

	public ConsentStatementListCriteria statement(String statement) {
		this.statement = statement;
		return self();
	}
}
