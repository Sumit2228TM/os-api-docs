package com.krishagni.catissueplus.core.biospecimen.label.visit;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;

public class VisitSiteCodeLabelToken extends AbstractVisitLabelToken {

	public VisitSiteCodeLabelToken() {
		this.name = "SITE_CODE";
	}

	@Override
	public String getLabel(Visit visit, String... args) {
		if (visit.getSite() == null || StringUtils.isBlank(visit.getSite().getCode())) {
			return StringUtils.EMPTY;
		}

		return visit.getSite().getCode();
	}
}
