package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum ConsentStatementErrorCode implements ErrorCode {
	NOT_FOUND,
	
	CODE_REQUIRED,

	REQUIRED,
	
	DUP_CODE,

	DUP;
	
	@Override
	public String code() {
		return "CONSENT_STATEMENT_" + this.name();
	}
}
