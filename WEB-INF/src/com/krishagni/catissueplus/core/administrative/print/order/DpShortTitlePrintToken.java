package com.krishagni.catissueplus.core.administrative.print.order;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrderItem;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public class DpShortTitlePrintToken extends AbstractLabelTmplToken implements LabelTmplToken {
	@Override
	public String getName() {
		return "order_dp_short_title";
	}

	@Override
	public String getReplacement(Object object) {
		DistributionOrderItem item = (DistributionOrderItem) object;
		return item.getOrder().getDistributionProtocol().getShortTitle();
	}
}
