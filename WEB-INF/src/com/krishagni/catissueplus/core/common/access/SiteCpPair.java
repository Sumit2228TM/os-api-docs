package com.krishagni.catissueplus.core.common.access;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SiteCpPair {
	private String resource;

	private Long instituteId;

	private Long siteId;

	private Long cpId;

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public Long getInstituteId() {
		return instituteId;
	}

	public void setInstituteId(Long instituteId) {
		this.instituteId = instituteId;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public boolean isAllowed(SiteCpPair other) {
		return (getSiteId() != null && getSiteId().equals(other.getSiteId())) ||
			(getSiteId() == null && getInstituteId().equals(other.getInstituteId()));
	}

	public SiteCpPair copy() {
		return make(getResource(), getInstituteId(), getSiteId(), getCpId());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SiteCpPair that = (SiteCpPair) o;
		return Objects.equals(getResource(), that.getResource()) &&
			Objects.equals(getInstituteId(), that.getInstituteId()) &&
			Objects.equals(getSiteId(), that.getSiteId()) &&
			Objects.equals(getCpId(), that.getCpId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getResource(), getInstituteId(), getSiteId(), getCpId());
	}

	public static SiteCpPair make(Long instituteId, Long siteId, Long cpId) {
		return make(null, instituteId, siteId, cpId);
	}

	public static SiteCpPair make(String resource, Long instituteId, Long siteId, Long cpId) {
		SiteCpPair result = new SiteCpPair();
		result.setResource(resource);
		result.setInstituteId(instituteId);
		result.setSiteId(siteId);
		result.setCpId(cpId);
		return result;
	}

	public static boolean contains(Collection<SiteCpPair> domainSites, SiteCpPair testSite) {
		return contains(domainSites, Collections.singleton(testSite));
	}

	public static boolean contains(Collection<SiteCpPair> domainSites, Collection<SiteCpPair> testSites) {
		return contains(domainSites, testSites, false);
	}

	public static boolean containsAll(Collection<SiteCpPair> domainSites, Collection<SiteCpPair> testSites) {
		return contains(domainSites, testSites, true);
	}

	public static Map<String, Set<SiteCpPair>> segregateByResources(Collection<SiteCpPair> siteCps) {
		Map<String, Set<SiteCpPair>> result = new HashMap<>();
		for (SiteCpPair siteCp : siteCps) {
			Set<SiteCpPair> resourceSiteCps = result.computeIfAbsent(siteCp.getResource(), (k) -> new HashSet<>());
			resourceSiteCps.add(siteCp);
		}

		return result;
	}

	private static boolean contains(Collection<SiteCpPair> domainSites, Collection<SiteCpPair> testSites, boolean allSites) {
		boolean result = true;
		for (SiteCpPair testSite : testSites) {
			boolean allowed = false;
			for (SiteCpPair domainSite : domainSites) {
				if (!Objects.equals(testSite.getResource(), domainSite.getResource())) {
					continue;
				}

				if (testSite.getSiteId() == null) {
					allowed = (!allSites || domainSite.getSiteId() == null) && domainSite.getInstituteId().equals(testSite.getInstituteId());
				} else {
					allowed = (domainSite.getSiteId() != null && domainSite.getSiteId().equals(testSite.getSiteId())) ||
						(domainSite.getSiteId() == null && domainSite.getInstituteId().equals(testSite.getInstituteId()));
				}

				if (allowed) {
					break;
				}
			}

			if (allSites) {
				if (!allowed) {
					result = false;
					break;
				} else {
					result = true;
				}
			} else {
				if (allowed) {
					result = true;
					break;
				} else {
					result = false;
				}
			}
		}

		return result;
	}
}
