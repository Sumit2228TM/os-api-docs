package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class DistributionOrderItemListCriteria extends AbstractListCriteria<DistributionOrderItemListCriteria> {

	private Long orderId;

	private boolean storedInContainers;

	@Override
	public DistributionOrderItemListCriteria self() {
		return this;
	}

	public Long orderId() {
		return orderId;
	}

	public DistributionOrderItemListCriteria orderId(Long orderId) {
		this.orderId = orderId;
		return self();
	}

	public boolean storedInContainers() {
		return storedInContainers;
	}

	public DistributionOrderItemListCriteria storedInContainers(boolean storedInContainers) {
		this.storedInContainers = storedInContainers;
		return self();
	}
}
