package com.krishagni.catissueplus.core.biospecimen.label.visit;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;

public class VisitClinicalStatusLabelToken extends AbstractVisitLabelToken {

	public VisitClinicalStatusLabelToken() {
		this.name = "CLINICAL_STATUS";
	}

	@Override
	public String getLabel(Visit visit, String... args) {
		PermissibleValue clinicalStatus = visit.getClinicalStatus();
		return clinicalStatus != null ? clinicalStatus.getValue() : StringUtils.EMPTY;
	}
}
