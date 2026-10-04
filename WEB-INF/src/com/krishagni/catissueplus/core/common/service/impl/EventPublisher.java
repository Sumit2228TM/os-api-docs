package com.krishagni.catissueplus.core.common.service.impl;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import com.krishagni.catissueplus.core.common.events.EventCode;
import com.krishagni.catissueplus.core.common.events.OpenSpecimenEvent;

@Configurable
public class EventPublisher implements ApplicationEventPublisherAware {
	
	private ApplicationEventPublisher publisher;
	
	private static EventPublisher instance = new EventPublisher();
	
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}
	
	public static EventPublisher getInstance() {
		return instance;
	}
	
	public <T> void publish(EventCode eventCode, T eventData) {
		publisher.publishEvent(new OpenSpecimenEvent<>(eventCode, eventData));
	}

	public <T extends ApplicationEvent> void publish(T event) {
		publisher.publishEvent(event);
	}
}
