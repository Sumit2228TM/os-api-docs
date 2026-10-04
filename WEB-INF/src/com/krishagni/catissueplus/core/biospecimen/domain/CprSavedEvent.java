package com.krishagni.catissueplus.core.biospecimen.domain;

import com.krishagni.catissueplus.core.common.events.OpenSpecimenEvent;

public class CprSavedEvent extends OpenSpecimenEvent<CollectionProtocolRegistration> {
	public CprSavedEvent(CollectionProtocolRegistration cpr) {
		super(null, cpr);
	}
}
