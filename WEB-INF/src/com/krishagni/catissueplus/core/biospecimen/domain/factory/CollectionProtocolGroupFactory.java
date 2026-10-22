package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolGroup;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolGroupDetail;

public interface CollectionProtocolGroupFactory {
	CollectionProtocolGroup createGroup(CollectionProtocolGroupDetail input);
}
