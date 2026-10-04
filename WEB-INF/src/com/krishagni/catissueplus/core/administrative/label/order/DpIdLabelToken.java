package com.krishagni.catissueplus.core.administrative.label.order;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrderItem;

public class DpIdLabelToken extends AbstractOrderItemLabelToken {

	public DpIdLabelToken() {
		this.name = "DP_ID";
	}

	@Override
	public String getLabel(DistributionOrderItem orderItem) {
		return orderItem.getOrder().getDistributionProtocol().getId().toString();
	}
}
