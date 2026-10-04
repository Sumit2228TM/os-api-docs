package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum UserGroupErrorCode implements ErrorCode {
	ID_NAME_REQ,

	NOT_FOUND,

	NAME_REQ,

	DUP_NAME,

	DESC_REQ,

	INST_REQ,

	USER_NOT_OF_INST;

	@Override
	public String code() {
		return "USER_GROUP_" + name();
	}
}
