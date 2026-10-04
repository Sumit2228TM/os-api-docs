package com.krishagni.catissueplus.core.query;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum  ListError implements ErrorCode {

	NAME_REQ,

	INVALID,

	NO_CFG,

	NO_COLUMNS,

	NO_CRITERIA,

	INVALID_FILTERS,

	INVALID_FIELD;

	@Override
	public String code() {
		return "LIST_" + name();
	}
}
