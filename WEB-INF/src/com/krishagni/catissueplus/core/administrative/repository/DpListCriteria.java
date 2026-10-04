package com.krishagni.catissueplus.core.administrative.repository;

import java.util.Set;

import com.krishagni.catissueplus.core.common.access.SiteCpPair;
import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class DpListCriteria extends AbstractListCriteria<DpListCriteria> {
	
	private String title;
	
	private Long piId;

	private String irbId;

	private String irbIdLike;
	
	private String receivingInstitute;

	private String receivingSite;

	private String cpShortTitle;
	
	private Set<SiteCpPair> sites;

	private boolean excludeExpiredDps;
	
	private String activityStatus;

	@Override
	public DpListCriteria self() {
		return this;
	}
	
	public String title() {
		return this.title;
	}
	
	public DpListCriteria title(String title) {
		this.title = title;
		return self();
	}
	
	public Long piId() {
		return this.piId;
	}
	
	public DpListCriteria piId(Long piId) {
		this.piId = piId;
		return self();
	}

	public String irbId() {
		return irbId;
	}

	public DpListCriteria irbId(String irbId) {
		this.irbId = irbId;
		return self();
	}

	public String irbIdLike() {
		return irbIdLike;
	}

	public DpListCriteria irbIdLike(String irbIdLike) {
		this.irbIdLike = irbIdLike;
		return self();
	}
	
	public String receivingInstitute() {
		return receivingInstitute;
	}
	
	public DpListCriteria receivingInstitute(String receivingInstitute) {
		this.receivingInstitute = receivingInstitute;
		return self();
	}

	public String receivingSite() {
		return receivingSite;
	}

	public DpListCriteria receivingSite(String receivingSite) {
		this.receivingSite = receivingSite;
		return self();
	}

	public String cpShortTitle() {
		return cpShortTitle;
	}

	public DpListCriteria cpShortTitle(String cpShortTitle) {
		this.cpShortTitle = cpShortTitle;
		return self();
	}
	
	public Set<SiteCpPair> sites() {
		return sites;
	}
	
	public DpListCriteria sites(Set<SiteCpPair> sites) {
		this.sites = sites;
		return self();
	}

	public boolean excludeExpiredDps() {
		return excludeExpiredDps;
	}

	public DpListCriteria excludeExpiredDps(boolean excludeExpiredDps) {
		this.excludeExpiredDps = excludeExpiredDps;
		return self();
	}
	
	public String activityStatus() {
		return this.activityStatus;
	}
	
	public DpListCriteria activityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
		return self();
	}
}
