package com.krishagni.catissueplus.core.administrative.label.container;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.domain.AbstractUniqueIdToken;

public class SiteUniqueIdLabelToken extends AbstractUniqueIdToken<StorageContainer> {

	@Autowired
	private DaoFactory daoFactory;

	public SiteUniqueIdLabelToken() {
		this.name = "SITE_UID";
	}

	@Override
	public Number getUniqueId(StorageContainer container, String... args) {
		return daoFactory.getUniqueIdGenerator().getUniqueId(getName(), container.getSite().getId().toString());
	}
}
