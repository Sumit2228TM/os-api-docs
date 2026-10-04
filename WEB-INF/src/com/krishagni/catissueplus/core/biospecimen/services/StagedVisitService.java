package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.StagedVisitDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface StagedVisitService {
	ResponseEvent<StagedVisitDetail> saveOrUpdateVisit(RequestEvent<StagedVisitDetail> req);
}
