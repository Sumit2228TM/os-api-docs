package com.krishagni.catissueplus.core.biospecimen.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class SpecimenKitListCriteria extends AbstractListCriteria<SpecimenKitListCriteria> {

	private Long cpId;

	private String cpShortTitle;

	private String cpTitle;

	@Override
	public SpecimenKitListCriteria self() {
		return this;
	}

	public Long cpId() {
		return cpId;
	}

	public SpecimenKitListCriteria cpId(Long cpId) {
		this.cpId = cpId;
		return self();
	}

	public String cpShortTitle() {
		return cpShortTitle;
	}

	public SpecimenKitListCriteria cpShortTitle(String cpShortTitle) {
		this.cpShortTitle = cpShortTitle;
		return self();
	}

	public String cpTitle() {
		return cpTitle;
	}

	public SpecimenKitListCriteria cpTitle(String cpTitle) {
		this.cpTitle = cpTitle;
		return self();
	}
}
