package com.krishagni.catissueplus.core.common.service;

import com.krishagni.catissueplus.core.common.domain.MessageLog;

public interface MessageHandler {
	void onStart();

	String process(MessageLog log);

	void onComplete();
}
