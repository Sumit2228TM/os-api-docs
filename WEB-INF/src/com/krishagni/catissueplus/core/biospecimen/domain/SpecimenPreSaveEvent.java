package com.krishagni.catissueplus.core.biospecimen.domain;

import com.krishagni.catissueplus.core.common.events.OpenSpecimenEvent;

public class SpecimenPreSaveEvent extends OpenSpecimenEvent<Specimen> {

	private static final long serialVersionUID = 8835609958815050454L;

	private Specimen oldSpecimen;

	private Specimen newSpecimen;

	public SpecimenPreSaveEvent(Specimen oldSpmn, Specimen newSpmn) {
		super(null, newSpmn);
		this.oldSpecimen = oldSpmn;
		this.newSpecimen = newSpmn;
	}

	public Specimen getOldSpecimen() {
		return oldSpecimen;
	}

	public Specimen getNewSpecimen() {
		return newSpecimen;
	}
}
