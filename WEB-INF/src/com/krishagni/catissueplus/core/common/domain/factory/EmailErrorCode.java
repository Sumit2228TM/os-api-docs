package com.krishagni.catissueplus.core.common.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum EmailErrorCode implements ErrorCode {
	UNABLE_TO_SEND,

	NOTIFS_ARE_DISABLED,

	ADMIN_EMAIL_REQ;

	@Override
	public String code() {
		return "EMAIL_" + this.name();
	}
}
