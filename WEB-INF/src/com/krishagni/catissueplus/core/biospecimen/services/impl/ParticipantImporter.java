package com.krishagni.catissueplus.core.biospecimen.services.impl;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CprErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.ParticipantService;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.CommonErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.services.impl.ExtensionsUtil;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class ParticipantImporter implements ObjectImporter<ParticipantDetail, ParticipantDetail> {
	private DaoFactory daoFactory;
	
	private ParticipantService participantSvc;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setParticipantSvc(ParticipantService participantSvc) {
		this.participantSvc = participantSvc;
	}

	@Override
	public ResponseEvent<ParticipantDetail> importObject(RequestEvent<ImportObjectDetail<ParticipantDetail>> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();

			ImportObjectDetail<ParticipantDetail> detail = req.getPayload();

			ParticipantDetail participant = detail.getObject();
			ExtensionsUtil.initFileFields(detail.getUploadedFilesDir(), participant.getExtensionDetail());
			if (StringUtils.isBlank(participant.getSource())) {
				participant.setSource(Participant.DEF_SOURCE);
			}

			if (!detail.isCreate()) {
				setParticipantId(participant);
				return participantSvc.patchParticipant(new RequestEvent<>(participant));
			} else {
				return ResponseEvent.userError(
					CommonErrorCode.INVALID_INPUT,
					"Participants can only be edited! Please consider using participant registrations template");
			}
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private void setParticipantId(ParticipantDetail detail) {
		String cpShortTitle = detail.getCpShortTitle();
		String ppid = detail.getPpid();
		if (StringUtils.isBlank(cpShortTitle) || StringUtils.isBlank(ppid)) { 
			return;
		}
		
		CollectionProtocolRegistration cpr = daoFactory.getCprDao().getCprByCpShortTitleAndPpid(cpShortTitle, ppid);
		if (cpr == null) {
			throw OpenSpecimenException.userError(CprErrorCode.M_NOT_FOUND, cpShortTitle + ":" + ppid, 1);
		}
		
		detail.setId(cpr.getParticipant().getId());
		detail.setCpId(cpr.getCollectionProtocol().getId());
	}
}
