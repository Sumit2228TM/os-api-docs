package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class ParentSpecimenLabelToken extends AbstractSpecimenLabelToken { 

	public ParentSpecimenLabelToken() {
		this.name = "PSPEC_LABEL"; 
	}
	
	@Override
	public String getLabel(Specimen specimen) {
		return specimen.getParentSpecimen() == null ? StringUtils.EMPTY : specimen.getParentSpecimen().getLabel();
	}
}
