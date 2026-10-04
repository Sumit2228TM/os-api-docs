package com.krishagni.catissueplus.core.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;
import com.krishagni.catissueplus.core.common.service.MessageLogService;

@Configurable
public class PendingMessagesProcessorTask implements ScheduledTask {

	@Autowired
	private MessageLogService msgLogSvc;

	@Override
	public void doJob(ScheduledJobRun jobRun)
	throws Exception {
		msgLogSvc.retryPendingMessages();
	}
}
