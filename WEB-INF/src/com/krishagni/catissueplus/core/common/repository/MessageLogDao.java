package com.krishagni.catissueplus.core.common.repository;

import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.common.domain.MessageLog;
import com.krishagni.catissueplus.core.common.events.MessageLogCriteria;

public interface MessageLogDao extends Dao<MessageLog> {
	List<MessageLog> getMessages(MessageLogCriteria crit);

	long getMessagesCount(MessageLogCriteria crit);

	int deleteOldMessages(Date olderThanDt);
}
