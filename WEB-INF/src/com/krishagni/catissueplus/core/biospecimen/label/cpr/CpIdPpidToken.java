package com.krishagni.catissueplus.core.biospecimen.label.cpr;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;

public class CpIdPpidToken extends AbstractPpidToken {

	public CpIdPpidToken() {
		this.name = "CP_ID";
	}

	@Override
	public String getLabel(CollectionProtocolRegistration cpr, String... args) {
		return cpr.getCollectionProtocol().getId().toString();
	}
}
