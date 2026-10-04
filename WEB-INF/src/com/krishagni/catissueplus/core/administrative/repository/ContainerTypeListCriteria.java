package com.krishagni.catissueplus.core.administrative.repository;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ContainerTypeListCriteria extends AbstractListCriteria<ContainerTypeListCriteria> {

	private List<String> canHold;

	@Override
	public ContainerTypeListCriteria self() {
		return this;
	}

	public List<String> canHold() {
		return canHold;
	}

	public ContainerTypeListCriteria canHold(String canHold) {
		if (StringUtils.isNotBlank(canHold)) {
			this.canHold = Collections.singletonList(canHold);
		}

		return self();
	}

	public ContainerTypeListCriteria canHold(List<String> canHold) {
		this.canHold = canHold;
		return self();
	}
}
