package com.krishagni.catissueplus.core.administrative.print.order;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrderItem;
import com.krishagni.catissueplus.core.common.PvAttributes;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;
import com.krishagni.catissueplus.core.common.util.PvUtil;

public class PathStatusPrintToken extends AbstractLabelTmplToken implements LabelTmplToken {
	@Override
	public String getName() {
		return "order_item_path_status";
	}

	@Override
	public String getReplacement(Object object) {
		DistributionOrderItem item = (DistributionOrderItem) object;
		return PvUtil.getInstance().getAbbr(item.getSpecimen().getPathologicalStatus(), null);
	}
}
