package com.krishagni.catissueplus.core.biospecimen.domain;

import com.krishagni.catissueplus.core.common.events.OpenSpecimenEvent;

public class SpecimenSavedEvent extends OpenSpecimenEvent<Specimen> {
	public SpecimenSavedEvent(Specimen specimen) {
		super(null, specimen);
	}
}
