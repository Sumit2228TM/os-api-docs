package com.krishagni.catissueplus.core.exporter.domain;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum  ExportErrorCode implements ErrorCode {
	INVALID_OBJECT_TYPE,

	NO_GEN_FOR_OBJECT_TYPE,

	JOB_NOT_FOUND;

	@Override
	public String code() {
		return "EXPORT_" + name();
	}
}
