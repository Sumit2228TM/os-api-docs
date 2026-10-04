package com.krishagni.catissueplus.core.administrative.label.order;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrderItem;

public class OrderIdLabelToken extends AbstractOrderItemLabelToken {

	public OrderIdLabelToken() {
		this.name = "ORDER_ID";
	}

	@Override
	public String getLabel(DistributionOrderItem orderItem) {
		return orderItem.getOrder().getId().toString();
	}
}
