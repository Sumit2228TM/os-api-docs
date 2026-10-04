package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.UserGroup;
import com.krishagni.catissueplus.core.common.ListenAttributeChanges;
import com.krishagni.catissueplus.core.common.events.UserSummary;

@ListenAttributeChanges
public class UserGroupDetail extends UserGroupSummary {
	private List<UserSummary> users;

	public List<UserSummary> getUsers() {
		return users;
	}

	public void setUsers(List<UserSummary> users) {
		this.users = users;
	}

	public static UserGroupDetail from(UserGroup group) {
		return from(group, true);
	}

	public static UserGroupDetail from(UserGroup group, boolean includeUsers) {
		UserGroupDetail result = new UserGroupDetail();
		result.setId(group.getId());
		result.setName(group.getName());
		result.setDescription(group.getDescription());
		result.setInstitute(group.getInstitute().getName());
		result.setActivityStatus(group.getActivityStatus());

		if (includeUsers) {
			result.setUsers(UserSummary.from(group.getUsers()));
		}

		return result;
	}
}
