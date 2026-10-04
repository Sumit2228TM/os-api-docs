package com.krishagni.catissueplus.core.biospecimen.domain;

import com.krishagni.catissueplus.core.common.events.OpenSpecimenEvent;

public class VisitPreSaveEvent extends OpenSpecimenEvent<Visit> {
	private static final long serialVersionUID = 8938288205079024404L;

	private Visit oldVisit;

	private Visit newVisit;

	public VisitPreSaveEvent(Visit oldVisit, Visit newVisit) {
		super(null, newVisit);
		this.oldVisit = oldVisit;
		this.newVisit = newVisit;
	}

	public Visit getOldVisit() {
		return oldVisit;
	}

	public Visit getNewVisit() {
		return newVisit;
	}
}
