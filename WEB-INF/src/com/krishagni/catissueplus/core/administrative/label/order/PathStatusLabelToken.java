package com.krishagni.catissueplus.core.administrative.label.order;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrderItem;
import com.krishagni.catissueplus.core.common.PvAttributes;
import com.krishagni.catissueplus.core.common.util.PvUtil;

public class PathStatusLabelToken extends AbstractOrderItemLabelToken {
	public PathStatusLabelToken() {
		this.name = "SP_PATH_STATUS";
	}

	@Override
	public String getLabel(DistributionOrderItem orderItem) {
		return PvUtil.getInstance().getAbbr(orderItem.getSpecimen().getPathologicalStatus(), StringUtils.EMPTY);
	}
}
