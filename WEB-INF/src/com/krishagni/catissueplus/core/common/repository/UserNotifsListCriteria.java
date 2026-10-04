package com.krishagni.catissueplus.core.common.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class UserNotifsListCriteria extends AbstractListCriteria<UserNotifsListCriteria> {
	private Long userId;
	
	@Override
	public UserNotifsListCriteria self() {
		return this;
	}

	public Long userId() {
		return userId;
	}
	
	public UserNotifsListCriteria userId(Long userId) {
		this.userId = userId;
		return self();
	}
}