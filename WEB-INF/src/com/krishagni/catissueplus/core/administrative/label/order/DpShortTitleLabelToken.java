package com.krishagni.catissueplus.core.administrative.label.order;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrderItem;

public class DpShortTitleLabelToken extends AbstractOrderItemLabelToken {

	public DpShortTitleLabelToken() {
		this.name = "DP_SHORT_TITLE";
	}

	@Override
	public String getLabel(DistributionOrderItem orderItem) {
		return orderItem.getOrder().getDistributionProtocol().getShortTitle();
	}
}
