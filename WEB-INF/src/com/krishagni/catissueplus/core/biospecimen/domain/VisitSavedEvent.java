package com.krishagni.catissueplus.core.biospecimen.domain;

import com.krishagni.catissueplus.core.common.events.OpenSpecimenEvent;

public class VisitSavedEvent extends OpenSpecimenEvent<Visit> {
	public VisitSavedEvent(Visit visit) {
		super(null, visit);
	}
}
