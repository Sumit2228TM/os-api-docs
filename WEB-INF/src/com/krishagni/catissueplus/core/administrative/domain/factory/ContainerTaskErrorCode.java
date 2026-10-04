package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum ContainerTaskErrorCode implements ErrorCode {
	NOT_FOUND,

	DUP_NAME,

	NAME_REQ;

	@Override
	public String code() {
		return "CONTAINER_TASK_" + name();
	}
}
