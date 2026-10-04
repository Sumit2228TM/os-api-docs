package com.krishagni.catissueplus.core.auth.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.ForgotPasswordToken;
import com.krishagni.catissueplus.core.administrative.domain.Password;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.domain.Notification;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.EmailUtil;
import com.krishagni.catissueplus.core.common.util.MessageUtil;
import com.krishagni.catissueplus.core.common.util.NotifUtil;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

@Configurable
public class OldPasswordNotification implements ScheduledTask {
	
	@Autowired
	private DaoFactory daoFactory;

	@Override
	@PlusTransactional
	public void doJob(ScheduledJobRun jobRun) throws Exception {
		int passwdExpiryDays = ConfigUtil.getInstance().getIntSetting("auth", "password_expiry_days", 0);
		if (passwdExpiryDays <= 0) {
			return;
		}

		passwdExpiryDays -= 5;
		if (passwdExpiryDays < 0) {
			passwdExpiryDays = 0;
		}

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -passwdExpiryDays);
		Date lastUpdateDate = Utility.chopSeconds(cal.getTime());

		List<Password> passwords = daoFactory.getUserDao().getPasswordsUpdatedBefore(lastUpdateDate);

		List<User> expiredPasswds = new ArrayList<>();
		for (Password passwd : passwords) {
			long daysToExpire = 5 - Utility.daysBetween(Utility.chopTime(passwd.getUpdationDate()), lastUpdateDate);
			if (daysToExpire <= 0) {
				expiredPasswds.add(passwd.getUser());
			} else {
				notifyUserPasswordExpiry(passwd.getUser(), daysToExpire);
			}
		}

		if (expiredPasswds.isEmpty()) {
			return;
		}

		daoFactory.getUserDao().updateStatus(expiredPasswds, Status.ACTIVITY_STATUS_EXPIRED.getStatus());
		expiredPasswds.forEach(user -> notifyUserPasswordExpiry(user, 0));
	}
	
	private void notifyUserPasswordExpiry(User user, long daysLeft) {
		Map<String, Object> emailProps = new HashMap<>();
		emailProps.put("daysLeft", daysLeft);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, (int)daysLeft);
		Date expiryDate = Utility.chopTime(cal.getTime());
		emailProps.put("expiryDate", expiryDate);

		emailProps.put("token", getPasswordUpdateToken(user));
		emailProps.put("user", user);
		emailProps.put("ccAdmin", false);
		emailProps.put("ignoreDnd", true);
		String[] rcpts = {user.getEmailAddress()};
		EmailUtil.getInstance().sendEmail(OLD_PASSWORD_NOTIF, rcpts, null, emailProps);

		if (daysLeft <= 0) {
			return;
		}

		String [] subjParams = {Utility.getDateString(expiryDate)};
		Notification notif = new Notification();
		notif.setEntityId(user.getId());
		notif.setEntityType(User.getEntityName());
		notif.setCreatedBy(daoFactory.getUserDao().getSystemUser());
		notif.setCreationTime(Calendar.getInstance().getTime());
		notif.setOperation("UPDATE");
		notif.setMessage(MessageUtil.getInstance().getMessage("user_password_expiry_notif", subjParams));
		NotifUtil.getInstance().notify(notif, Collections.singletonMap("user-password-change", Collections.singletonList(user)));
	}
	
	private String getPasswordUpdateToken(User user) {
		UserDao userDao = daoFactory.getUserDao();

		ForgotPasswordToken oldToken = userDao.getFpTokenByUser(user.getId());
		if (oldToken != null) {
			userDao.deleteFpToken(oldToken);
		}
		
		ForgotPasswordToken token = new ForgotPasswordToken(user);
		userDao.saveFpToken(token);
		return token.getToken();
	}

	private static final String OLD_PASSWORD_NOTIF = "old_password_notification";
}
