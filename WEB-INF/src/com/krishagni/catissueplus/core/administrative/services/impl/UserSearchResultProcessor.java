package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.service.impl.AbstractSearchResultProcessor;
import com.krishagni.catissueplus.core.common.util.AuthUtil;

public class UserSearchResultProcessor extends AbstractSearchResultProcessor {
	@Override
	public String getEntity() {
		return User.getEntityName();
	}

	@Override
	protected String getQuery() {
		return String.format(QUERY, AuthUtil.getCurrentUserInstitute().getId());
	}

	private static final String QUERY =
		"select " +
		"  k.identifier, k.entity, k.entity_id, k.name, k.value " +
		"from " +
		"  os_search_entity_keywords k " +
		"  inner join catissue_user u on u.identifier = k.entity_id " +
		"where " +
		"  k.value like ? and " +
		"  k.identifier > ? and " +
		"  k.entity = 'user' and " +
		"  k.status = 1 and " +
		"  u.institute_id = %d " +
		"order by " +
		"  k.identifier";
}
