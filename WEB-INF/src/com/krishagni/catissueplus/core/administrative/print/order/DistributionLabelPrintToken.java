package com.krishagni.catissueplus.core.administrative.print.order;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrderItem;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public class DistributionLabelPrintToken extends AbstractLabelTmplToken implements LabelTmplToken {
	@Override
	public String getName() {
		return "order_item_label";
	}

	@Override
	public String getReplacement(Object object) {
		DistributionOrderItem orderItem = (DistributionOrderItem) object;
		return orderItem.getLabel();
	}
}
