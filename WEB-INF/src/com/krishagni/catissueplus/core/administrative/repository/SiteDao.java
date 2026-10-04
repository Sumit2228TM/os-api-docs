
package com.krishagni.catissueplus.core.administrative.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface SiteDao extends Dao<Site> {

	List<Site> getSites(SiteListCriteria crit);

	Long getSitesCount(SiteListCriteria crit);

	List<Site> getSitesByNames(Collection<String> siteNames);
	
	Site getSiteByName(String siteName);

	Site getSiteByCode(String code);
	
	//
	// At present this is only returning count of CPs by site
	// in future this would be extended to return other stats
	// related to site
	//
	Map<Long, Integer> getCpCountBySite(Collection<Long> siteIds);


	Map<String, Object> getSiteIds(String key, Object value);

	boolean isAffiliatedToUserInstitute(Long siteId, Long userId);
}
