package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.DpRequirement;
import com.krishagni.catissueplus.core.administrative.events.DpRequirementDetail;

public interface DpRequirementFactory {
	DpRequirement createRequirement(DpRequirementDetail detail);

	DpRequirement createRequirement(DpRequirement existing, DpRequirementDetail detail);
}
