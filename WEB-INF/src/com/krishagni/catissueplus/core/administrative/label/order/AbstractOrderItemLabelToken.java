package com.krishagni.catissueplus.core.administrative.label.order;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrderItem;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public abstract class AbstractOrderItemLabelToken extends AbstractLabelTmplToken implements LabelTmplToken {

	protected String name;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getReplacement(Object object) {
		if (!(object instanceof DistributionOrderItem)) {
			throw new IllegalArgumentException("Invalid input object type: " + object.getClass().getName());
		}

		return getLabel((DistributionOrderItem) object);

	}

	public abstract String getLabel(DistributionOrderItem orderItem);
}
