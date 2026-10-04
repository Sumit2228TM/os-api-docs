package com.krishagni.catissueplus.core.biospecimen.services;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.ConsentStatementDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.ConsentStatementListCriteria;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface ConsentStatementService {
	ResponseEvent<List<ConsentStatementDetail>> getStatements(RequestEvent<ConsentStatementListCriteria> req);
	
	ResponseEvent<Long> getStatementsCount(RequestEvent<ConsentStatementListCriteria> req);
	
	ResponseEvent<ConsentStatementDetail> getStatement(RequestEvent<EntityQueryCriteria> req);
	
	ResponseEvent<ConsentStatementDetail> createStatement(RequestEvent<ConsentStatementDetail> req);
	
	ResponseEvent<ConsentStatementDetail> updateStatement(RequestEvent<ConsentStatementDetail> req);
}
