package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.common.service.impl.AbstractSearchResultProcessor;
import com.krishagni.catissueplus.core.common.util.AuthUtil;

public class SiteSearchResultProcessor extends AbstractSearchResultProcessor {
	@Override
	public String getEntity() {
		return Site.getEntityName();
	}

	@Override
	protected String getQuery() {
		if (!AuthUtil.isInstituteAdmin()) {
			return null;
		}

		return String.format(QUERY, AuthUtil.getCurrentUserInstitute().getId());
	}

	private static final String QUERY =
		"select " +
		"  k.identifier, k.entity, k.entity_id, k.name, k.value " +
		"from " +
		"  os_search_entity_keywords k " +
		"  inner join catissue_site s on s.identifier = k.entity_id " +
		"where " +
		"  k.value like ? and " +
		"  k.identifier > ? and " +
		"  k.entity = 'site' and " +
		"  k.status = 1 and " +
		"  s.institute_id = %d " +
		"order by " +
		"  k.identifier";
}