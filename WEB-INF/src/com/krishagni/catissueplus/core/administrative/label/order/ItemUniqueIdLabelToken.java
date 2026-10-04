package com.krishagni.catissueplus.core.administrative.label.order;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrderItem;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.domain.AbstractUniqueIdToken;

public class ItemUniqueIdLabelToken extends AbstractUniqueIdToken<DistributionOrderItem> {

	@Autowired
	private DaoFactory daoFactory;

	public ItemUniqueIdLabelToken() {
		this.name = "ITEM_UID";
	}

	@Override
	public Number getUniqueId(DistributionOrderItem item, String... args) {
		return daoFactory.getUniqueIdGenerator().getUniqueId(getName(), item.getOrder().getId().toString());
	}
}
