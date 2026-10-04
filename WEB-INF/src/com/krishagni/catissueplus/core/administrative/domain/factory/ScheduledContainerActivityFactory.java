package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledContainerActivity;
import com.krishagni.catissueplus.core.administrative.events.ScheduledContainerActivityDetail;

public interface ScheduledContainerActivityFactory {
	ScheduledContainerActivity createActivity(ScheduledContainerActivityDetail input);
}
