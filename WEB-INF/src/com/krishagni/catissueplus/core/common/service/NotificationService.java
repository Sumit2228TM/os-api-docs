package com.krishagni.catissueplus.core.common.service;

import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.domain.Notification;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserNotificationDetail;
import com.krishagni.catissueplus.core.common.repository.UserNotifsListCriteria;

public interface NotificationService {
	ResponseEvent<List<UserNotificationDetail>> getUserNotifications(RequestEvent<UserNotifsListCriteria> req);

	ResponseEvent<Long> getUnreadNotificationsCount();

	ResponseEvent<Integer> markNotificationsAsRead(RequestEvent<Date> req);

	void addNotification(Notification notification, List<User> users);
}
