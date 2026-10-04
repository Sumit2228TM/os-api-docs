package com.krishagni.catissueplus.core.administrative.domain;

import com.krishagni.catissueplus.core.common.events.EventCode;

public enum UserEvent implements EventCode {
	CREATED,

	XTRA_AUTH;

	@Override
	public String code() {
		return "USER_" + name();
	}
}
