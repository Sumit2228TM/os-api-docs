package com.krishagni.catissueplus.core.biospecimen.domain;

import com.krishagni.catissueplus.core.common.events.OpenSpecimenEvent;

public class SprSavedEvent extends OpenSpecimenEvent<Visit> {
	public SprSavedEvent(Visit visit) {
		super(null, visit);
	}
}
