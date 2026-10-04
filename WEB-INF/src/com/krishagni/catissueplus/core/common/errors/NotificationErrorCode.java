package com.krishagni.catissueplus.core.common.errors;

public enum NotificationErrorCode implements ErrorCode {
	NOT_FOUND;
	
	@Override
	public String code() {
		return "NOTIFICATION_" + this.name();
	}
}
