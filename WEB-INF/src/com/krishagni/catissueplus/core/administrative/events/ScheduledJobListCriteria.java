package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ScheduledJobListCriteria extends AbstractListCriteria<ScheduledJobListCriteria> {

	private ScheduledJob.Type type;

	private Long userId;

	@Override
	public ScheduledJobListCriteria self() {
		return this;
	}

	public ScheduledJob.Type type() {
		return type;
	}

	public ScheduledJobListCriteria type(ScheduledJob.Type type) {
		this.type = type;
		return self();
	}

	public Long userId() {
		return userId;
	}

	public ScheduledJobListCriteria userId(Long userId) {
		this.userId = userId;
		return self();
	}
}
