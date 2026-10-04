package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;

public interface CollectionProtocolFactory {
	CollectionProtocol createCollectionProtocol(CollectionProtocolDetail cp);
	
	CollectionProtocol createCpCopy(CollectionProtocolDetail input, CollectionProtocol existing);
}
