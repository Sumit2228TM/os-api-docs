package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderErrorCode;
import com.krishagni.catissueplus.core.administrative.services.DistributionValidator;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class DistributionConsentValidator implements DistributionValidator {
	private static final String NAME = "consents";

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void validate(DistributionProtocol dp, List<Specimen> specimens, Map<String, Object> ctxt) {
		int stmtsCount = dp.getConsentTiers().size();
		if (stmtsCount <= 0) {
			return;
		}

		List<Long> spmnIds = specimens.stream().map(Specimen::getId).collect(Collectors.toList());
		List<String> nonConsentingLabels = daoFactory.getDistributionProtocolDao()
			.getNonConsentingSpecimens(dp.getId(), spmnIds, stmtsCount);
		if (nonConsentingLabels.isEmpty()) {
			return;
		}

		if (nonConsentingLabels.size() > 10) {
			nonConsentingLabels = nonConsentingLabels.subList(0, 10);
		}

		throw OpenSpecimenException.userError(DistributionOrderErrorCode.NON_CONSENTING_SPECIMENS, nonConsentingLabels);
	}
}
