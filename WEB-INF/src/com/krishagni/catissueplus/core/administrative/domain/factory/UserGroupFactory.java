package com.krishagni.catissueplus.core.administrative.domain.factory;

import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.UserGroup;
import com.krishagni.catissueplus.core.administrative.events.UserGroupDetail;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public interface UserGroupFactory {
	UserGroup createGroup(UserGroupDetail input);

	UserGroup createGroup(UserGroupDetail input, UserGroup existing);

	Set<User> getUsers(List<UserSummary> inputUsers);
}
