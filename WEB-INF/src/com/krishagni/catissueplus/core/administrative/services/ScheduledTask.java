package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;

public interface ScheduledTask {
	void doJob(ScheduledJobRun jobRun) throws Exception;
	
}
