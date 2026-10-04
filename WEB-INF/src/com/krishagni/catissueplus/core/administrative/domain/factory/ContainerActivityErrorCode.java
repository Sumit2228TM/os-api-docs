package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum ContainerActivityErrorCode implements ErrorCode {
	ST_DATE_REQ,

	CYCLE_INT_REQ,

	INVALID_CYCLE_INT,

	CYCLE_INT_UNIT_REQ,

	REM_INT_REQ,

	INVALID_REM_INT,

	REM_INT_UNIT_REQ,

	ASSIGNED_TO_REQ,

	TL_CONT_REQ,

	ID_REQ,

	NOT_FOUND,

	PERF_BY_REQ,

	DATE_REQ,

	NAME_REQ,

	DUP_NAME,

	INVALID_TIME_TAKEN;

	@Override
	public String code() {
		return "CONT_ACTIVITY_" + name();
	}
}
