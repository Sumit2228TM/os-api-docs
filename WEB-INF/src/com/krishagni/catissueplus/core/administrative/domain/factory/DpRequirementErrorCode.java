package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum DpRequirementErrorCode implements ErrorCode {
	NOT_FOUND,
	
	ALREADY_EXISTS,
	
	DP_REQUIRED,
	
	INVALID_SPECIMEN_COUNT,
	
	INVALID_QUANTITY,

	SPEC_PROPERTY_REQUIRED,

	INVALID_PATHOLOGY_STATUSES,

	INVALID_COST;
	
	@Override
	public String code() {
		return "DPR_" + this.name();
	}
}
