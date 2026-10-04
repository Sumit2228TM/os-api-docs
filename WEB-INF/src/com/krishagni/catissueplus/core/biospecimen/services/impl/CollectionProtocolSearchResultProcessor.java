package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.access.SiteCpPair;
import com.krishagni.catissueplus.core.common.service.impl.AbstractSearchResultProcessor;

public class CollectionProtocolSearchResultProcessor extends AbstractSearchResultProcessor {
	@Override
	public String getEntity() {
		return CollectionProtocol.getEntityName();
	}

	@Override
	public String getQuery() {
		Set<SiteCpPair> siteCps = AccessCtrlMgr.getInstance().getReadableSiteCps();
		if (siteCps.isEmpty()) {
			return null;
		}

		String joinCondition = "";
		List<String> clauses = new ArrayList<>();
		for (SiteCpPair siteCp : siteCps) {
			if (siteCp.getCpId() != null) {
				clauses.add("cp.identifier = " + siteCp.getCpId());
			} else {
				if (StringUtils.isBlank(joinCondition)) {
					joinCondition = CP_SITE_JOIN_COND;
				}

				if (siteCp.getSiteId() != null) {
					clauses.add("site.identifier = " + siteCp.getSiteId());
				} else {
					clauses.add("site.institute_id = " + siteCp.getInstituteId());
				}
			}
		}

		return String.format(QUERY, joinCondition, String.join(" or ", clauses));
	}

	private static final String QUERY =
		"select " +
		"  distinct k.identifier, k.entity, k.entity_id, k.name, k.value " +
		"from " +
		"  os_search_entity_keywords k " +
		"  inner join catissue_collection_protocol cp on cp.identifier = k.entity_id " +
		"  %s " +
		"where " +
		"  k.value like ? and " +
		"  k.identifier > ? and " +
		"  k.entity = 'collection_protocol' and " +
		"  k.status = 1 and " +
		"  (%s) " +
		"order by " +
		"  k.identifier";

	private static final String CP_SITE_JOIN_COND =
		"inner join catissue_site_cp cp_site on cp_site.collection_protocol_id = cp.identifier " +
		"inner join catissue_site site on site.identifier = cp_site.site_id ";
}