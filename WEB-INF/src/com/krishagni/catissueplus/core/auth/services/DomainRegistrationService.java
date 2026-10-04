
package com.krishagni.catissueplus.core.auth.services;

import java.util.List;

import com.krishagni.catissueplus.core.auth.events.AuthDomainDetail;
import com.krishagni.catissueplus.core.auth.events.AuthDomainSummary;
import com.krishagni.catissueplus.core.auth.events.ListAuthDomainCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface DomainRegistrationService {

	ResponseEvent<List<AuthDomainSummary>> getDomains(RequestEvent<ListAuthDomainCriteria> req);

	ResponseEvent<AuthDomainDetail> getDomain(RequestEvent<AuthDomainSummary> req);
	
	ResponseEvent<AuthDomainDetail> registerDomain(RequestEvent<AuthDomainDetail> req);
	
	ResponseEvent<AuthDomainDetail> updateDomain(RequestEvent<AuthDomainDetail> req);

	ResponseEvent<AuthDomainDetail> deleteDomain(RequestEvent<AuthDomainSummary> req);
}
