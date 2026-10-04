package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.common.service.impl.AbstractSearchResultProcessor;
import com.krishagni.catissueplus.core.common.util.AuthUtil;

public class InstituteSearchResultProcessor extends AbstractSearchResultProcessor {
	@Override
	public String getEntity() {
		return Institute.getEntityName();
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
		"  inner join catissue_institution i on i.identifier = k.entity_id " +
		"where " +
		"  k.value like ? and " +
		"  k.identifier > ? and " +
		"  k.entity = 'institute' and " +
		"  k.status = 1 and " +
		"  i.identifier = %d " +
		"order by " +
		"  k.identifier";
}
