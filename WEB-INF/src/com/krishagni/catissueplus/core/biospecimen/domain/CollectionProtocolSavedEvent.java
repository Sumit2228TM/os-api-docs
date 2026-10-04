package com.krishagni.catissueplus.core.biospecimen.domain;

import com.krishagni.catissueplus.core.common.events.OpenSpecimenEvent;

public class CollectionProtocolSavedEvent extends OpenSpecimenEvent<CollectionProtocol> {
	private static final long serialVersionUID = -7093777807758578388L;

	public CollectionProtocolSavedEvent(CollectionProtocol cp) {
		super(null, cp);
	}
}
