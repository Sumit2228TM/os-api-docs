
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;
import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.domain.Notification;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.EmailUtil;
import com.krishagni.catissueplus.core.common.util.MessageUtil;
import com.krishagni.catissueplus.core.common.util.NotifUtil;
import com.krishagni.catissueplus.core.common.util.Utility;

@Configurable
public class CpExpiringNotification implements ScheduledTask {
	
	private static final String CP_EXPIRING_NOTIFICATION_TMPL = "cp_expiring_notification";
	
	@Autowired
	private DaoFactory daoFactory;

	@Override
	@PlusTransactional
	public void doJob(ScheduledJobRun jobRun) throws Exception {
		Date intervalStartDate = getIntervalStartDate();
		int notificationDays = getNotificationDays();
		Date intervalEndDate = getIntervalEndDate(intervalStartDate, notificationDays);
		int repeatInterval = getRepeatInterval();
		
		List<CollectionProtocol> cps = daoFactory.getCollectionProtocolDao()
			.getExpiringCps(intervalStartDate, intervalEndDate);
		for (CollectionProtocol cp : cps) {
		  if (isEligibleForReminder(cp, intervalStartDate, notificationDays, repeatInterval)) {
		    notifyCpExpiryReminder(cp);
		  }
		}
	}
	
	private Date getIntervalStartDate() {
		return Utility.chopTime(new Date());
	}
	
	private int getNotificationDays() {
		return ConfigUtil.getInstance().getIntSetting(ConfigParams.MODULE, ConfigParams.CP_EXPIRY_REM_NOTIF, 0);
	}

	private Date getIntervalEndDate(Date intervalStartDate, int notificationDays) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(intervalStartDate);

		cal.add(Calendar.DATE, notificationDays);

		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}
	
	private int getRepeatInterval() {
		return ConfigUtil.getInstance().getIntSetting(ConfigParams.MODULE, ConfigParams.CP_EXPIRY_REM_REPT_INTER, 0);
	}
	
	private Boolean isEligibleForReminder(CollectionProtocol cp, Date intervalStartDate,
			int notificationDays, int repeatInterval) {

		long daysBeforeExpiry = Utility.daysBetween(intervalStartDate, Utility.chopTime(cp.getEndDate()));
		return daysBeforeExpiry == 0 || (notificationDays - daysBeforeExpiry) % repeatInterval == 0;
	}
	
	private void notifyCpExpiryReminder(CollectionProtocol cp) {
		Map<String, Object> emailProps = new HashMap<>();
		String[] subjParams = {cp.getShortTitle(), Utility.getDateString(cp.getEndDate(), true)};
		emailProps.put("$subject", subjParams);
		emailProps.put("cp", cp);
		emailProps.put("ccAdmin", false);


		Set<User> rcpts = new HashSet<>(AccessCtrlMgr.getInstance().getSuperAndSiteAdmins(null, cp));
		rcpts.add(cp.getPrincipalInvestigator());
		rcpts.addAll(cp.getCoordinators());

		for (User rcpt : rcpts) {
			emailProps.put("rcpt", rcpt);
			EmailUtil.getInstance().sendEmail(CP_EXPIRING_NOTIFICATION_TMPL, new String[] {rcpt.getEmailAddress()}, null, emailProps);
		}

		Notification notif = new Notification();
		notif.setEntityType(CollectionProtocol.getEntityName());
		notif.setEntityId(cp.getId());
		notif.setOperation("ALERT");
		notif.setMessage(MessageUtil.getInstance().getMessage(CP_EXPIRING_NOTIFICATION_TMPL.toLowerCase() + "_subj", subjParams));
		notif.setCreatedBy(AuthUtil.getCurrentUser());
		notif.setCreationTime(Calendar.getInstance().getTime());
		NotifUtil.getInstance().notify(notif, Collections.singletonMap("cp-overview", rcpts));
	}
}
