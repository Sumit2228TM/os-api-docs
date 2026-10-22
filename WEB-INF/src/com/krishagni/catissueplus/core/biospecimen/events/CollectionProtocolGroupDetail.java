package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolGroup;

public class CollectionProtocolGroupDetail {
	private Long id;

	private String name;

	private String activityStatus;

	private Set<CollectionProtocolSummary> cps = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Set<CollectionProtocolSummary> getCps() {
		return cps;
	}

	public void setCps(Set<CollectionProtocolSummary> cps) {
		this.cps = cps;
	}

	public static CollectionProtocolGroupDetail from(CollectionProtocolGroup group) {
		CollectionProtocolGroupDetail result = new CollectionProtocolGroupDetail();
		result.setId(group.getId());
		result.setName(group.getName());
		result.setCps(group.getCps().stream().map(CollectionProtocolSummary::from).collect(Collectors.toSet()));
		result.setActivityStatus(group.getActivityStatus());
		return result;
	}
}
