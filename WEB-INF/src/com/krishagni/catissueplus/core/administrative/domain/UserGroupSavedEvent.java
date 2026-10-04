package com.krishagni.catissueplus.core.administrative.domain;

import com.krishagni.catissueplus.core.common.events.OpenSpecimenEvent;

public class UserGroupSavedEvent extends OpenSpecimenEvent<UserGroup> {
	public UserGroupSavedEvent(UserGroup group) {
		super(null, group);
	}
}
