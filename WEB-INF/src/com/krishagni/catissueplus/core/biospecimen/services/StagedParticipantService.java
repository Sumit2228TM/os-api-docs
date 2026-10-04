package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.StagedParticipantDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface StagedParticipantService {
	ResponseEvent<StagedParticipantDetail> saveOrUpdateParticipant(RequestEvent<StagedParticipantDetail> req);
}
