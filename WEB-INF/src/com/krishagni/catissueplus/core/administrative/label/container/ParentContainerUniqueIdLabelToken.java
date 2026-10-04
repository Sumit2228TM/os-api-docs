package com.krishagni.catissueplus.core.administrative.label.container;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.domain.AbstractUniqueIdToken;

public class ParentContainerUniqueIdLabelToken extends AbstractUniqueIdToken<StorageContainer> {

	@Autowired
	private DaoFactory daoFactory;

	public ParentContainerUniqueIdLabelToken() {
		this.name = "PCONT_UID";
	}

	@Override
	public Number getUniqueId(StorageContainer container, String... args) {
		StorageContainer parent = container.getParentContainer();
		if (parent == null) {
			return null;
		}

		return daoFactory.getUniqueIdGenerator().getUniqueId(name, parent.getName());
	}
}
