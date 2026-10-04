package com.krishagni.catissueplus.core.biospecimen.label.cpr;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.domain.AbstractUniqueIdToken;

public class CpUniqueIdPpidToken extends AbstractUniqueIdToken<CollectionProtocolRegistration> {

	@Autowired
	private DaoFactory daoFactory;
	
	public CpUniqueIdPpidToken() {
		this.name = "CP_UID";
	}

	public Number getUniqueId(CollectionProtocolRegistration cpr, String ... args) {
		return daoFactory.getUniqueIdGenerator().getUniqueId("PPID", cpr.getCollectionProtocol().getId().toString());
	}
}
