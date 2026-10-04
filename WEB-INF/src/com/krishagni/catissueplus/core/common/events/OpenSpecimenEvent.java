package com.krishagni.catissueplus.core.common.events;

import java.util.Calendar;
import java.util.Date;

import org.springframework.context.ApplicationEvent;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.util.AuthUtil;

public class OpenSpecimenEvent<T> extends ApplicationEvent {
	private EventCode eventCode;
	
	private T eventData;
	
	private User user;
	
	private Date time;
	
	public OpenSpecimenEvent(EventCode eventCode, T eventData) {
		super(eventData);
		this.eventCode = eventCode;
		this.eventData = eventData;
		this.time = Calendar.getInstance().getTime();
		this.user = AuthUtil.getCurrentUser();
	}

	public EventCode getEventCode() {
		return eventCode;
	}

	public void setEventCode(EventCode eventCode) {
		this.eventCode = eventCode;
	}

	public T getEventData() {
		return eventData;
	}

	public void setEventData(T eventData) {
		this.eventData = eventData;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}	
}
