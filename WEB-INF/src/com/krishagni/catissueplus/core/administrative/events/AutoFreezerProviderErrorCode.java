package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum AutoFreezerProviderErrorCode implements ErrorCode {
	NOT_FOUND,

	NAME_REQ,

	DUP_NAME,

	CLASS_REQ,

	INVALID_CLASS;

	@Override
	public String code() {
		return "AUTO_FREEZER_PROV_" + this.name();
	}
}
