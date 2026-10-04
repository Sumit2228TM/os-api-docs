package com.krishagni.catissueplus.core.administrative.domain;

import com.krishagni.catissueplus.core.common.events.OpenSpecimenEvent;

public class ScheduledJobSavedEvent extends OpenSpecimenEvent<ScheduledJob> {
	private static final long serialVersionUID = 3481601025742573856L;

	public ScheduledJobSavedEvent(ScheduledJob job) {
		super(null, job);
	}
}
