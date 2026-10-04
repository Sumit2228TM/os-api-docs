package com.krishagni.catissueplus.core.biospecimen.domain;

import com.krishagni.catissueplus.core.common.events.OpenSpecimenEvent;

public class StagedParticipantSavedEvent extends OpenSpecimenEvent<StagedParticipant> {
	public StagedParticipantSavedEvent(StagedParticipant participant) {
		super(null, participant);
	}
}
