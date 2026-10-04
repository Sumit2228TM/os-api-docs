package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenList;
import com.krishagni.catissueplus.core.common.service.impl.AbstractSearchResultProcessor;
import com.krishagni.catissueplus.core.common.util.AuthUtil;

public class SpecimenListSearchResultProcessor extends AbstractSearchResultProcessor {
	@Override
	public String getEntity() {
		return SpecimenList.getEntityName();
	}

	@Override
	protected String getQuery() {
		return String.format(QUERY, AuthUtil.getCurrentUser().getId(), AuthUtil.getCurrentUser().getId());
	}

	private static final String QUERY =
		"select " +
		"  distinct k.identifier, k.entity, k.entity_id, k.name, k.value " +
		"from " +
		"  os_search_entity_keywords k " +
		"  inner join catissue_specimenlist_tags l on l.identifier = k.entity_id " +
		"  left join shared_specimenlist_tags su on su.tag_id = l.identifier " +
		"where " +
		"  k.value like ? and " +
		"  k.identifier > ? and " +
		"  k.entity = 'specimen_list' and " +
		"  k.status = 1 and " +
		"  (l.user_id = %d or su.user_id = %d)" +
		"order by " +
		"  k.identifier";
}
