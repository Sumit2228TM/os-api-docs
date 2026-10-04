package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.Set;

import com.krishagni.catissueplus.core.common.access.SiteCpPair;
import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class CpGroupListCriteria extends AbstractListCriteria<CpGroupListCriteria> {

	private Set<SiteCpPair> siteCps;

	private String cpShortTitle;

	@Override
	public CpGroupListCriteria self() {
		return this;
	}

	public Set<SiteCpPair> siteCps() {
		return siteCps;
	}

	public CpGroupListCriteria siteCps(Set<SiteCpPair> siteCps) {
		this.siteCps = siteCps;
		return self();
	}

	public String cpShortTitle() {
		return cpShortTitle;
	}

	public CpGroupListCriteria cpShortTitle(String cpShortTitle) {
		this.cpShortTitle = cpShortTitle;
		return self();
	}
}
