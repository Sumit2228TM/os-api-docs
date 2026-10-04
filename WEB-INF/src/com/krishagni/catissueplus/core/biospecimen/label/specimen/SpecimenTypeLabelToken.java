package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class SpecimenTypeLabelToken extends AbstractSpecimenLabelToken {

	private static final String ABBREVIATION = "abbreviation";

	public SpecimenTypeLabelToken() {
		this.name = "SP_TYPE";
	}

	@Override
	public String getLabel(Specimen specimen) {
		String abbr = specimen.getSpecimenType().getProps().get(ABBREVIATION);
		return StringUtils.isBlank(abbr) ? StringUtils.EMPTY : abbr;
	}
}
