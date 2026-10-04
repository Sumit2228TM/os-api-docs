package com.krishagni.catissueplus.core.common.domain;

import com.krishagni.catissueplus.core.common.events.EventCode;

public enum  PrintRuleEvent implements EventCode {
	CREATED,

	UPDATED,

	DELETED,

	CACHE_UPDATED;

	@Override
	public String code() {
		return "PRINT_RULE_" + name();
	}
}
