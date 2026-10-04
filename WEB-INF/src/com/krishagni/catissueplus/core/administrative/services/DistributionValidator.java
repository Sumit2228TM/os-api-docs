package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public interface DistributionValidator {
	String getName();

	void validate(DistributionProtocol dp, List<Specimen> specimens, Map<String, Object> ctxt);
}
