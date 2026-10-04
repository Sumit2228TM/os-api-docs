package com.krishagni.catissueplus.core.biospecimen.label.visit;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.util.PvUtil;

public class VisitClinicalStatusAbbrToken extends AbstractVisitLabelToken {

	public VisitClinicalStatusAbbrToken() {
		this.name = "CLINICAL_STATUS_ABBR";
	}

	@Override
	public String getLabel(Visit visit, String... args) {
		PermissibleValue clinicalStatus = visit.getClinicalStatus();
		return PvUtil.getInstance().getAbbr(clinicalStatus, StringUtils.EMPTY);
	}
}
