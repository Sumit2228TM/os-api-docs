package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.ConsentStatement;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentStatementDetail;

public interface ConsentStatementFactory {
	ConsentStatement createStatement(ConsentStatementDetail detail);
}
