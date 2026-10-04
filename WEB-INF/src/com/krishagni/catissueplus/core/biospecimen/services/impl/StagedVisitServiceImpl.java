package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.Calendar;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.StagedParticipant;
import com.krishagni.catissueplus.core.biospecimen.domain.StagedVisit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.StagedVisitDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.StagedVisitService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class StagedVisitServiceImpl implements StagedVisitService {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<StagedVisitDetail> saveOrUpdateVisit(RequestEvent<StagedVisitDetail> req) {
		try {
			StagedVisitDetail detail = req.getPayload();
			StagedVisit savedVisit = saveOrUpdateVisit(getMatchingVisit(detail), detail);
			return ResponseEvent.response(StagedVisitDetail.from(savedVisit));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private StagedVisit getMatchingVisit(StagedVisitDetail input) {
		StagedVisit visit = null;
		if (StringUtils.isNotBlank(input.getName())) {
			visit = daoFactory.getStagedVisitDao().getByName(input.getName());
		} else if (StringUtils.isNotBlank(input.getSurgicalPathologyNumber())) {
			visit = daoFactory.getStagedVisitDao().getBySprNo(input.getSurgicalPathologyNumber());
		}

		return visit;
	}

	private StagedVisit saveOrUpdateVisit(StagedVisit existing, StagedVisitDetail detail) {
		StagedVisit visit = createVisit(existing, detail);
		if (existing != null) {
			existing.update(visit);
			visit = existing;
		}

		daoFactory.getStagedVisitDao().saveOrUpdate(visit, true);
		return visit;
	}

	private StagedVisit createVisit(StagedVisit existing, StagedVisitDetail detail) {
		StagedVisit result = new StagedVisit();
		if (existing != null) {
			BeanUtils.copyProperties(existing, result);
		}

		setVisitAttrs(detail, result);
		return result;
	}

	private void setVisitAttrs(StagedVisitDetail detail, StagedVisit visit) {
		if (detail.getClinicalDiagnoses() == null) {
			detail.setClinicalDiagnoses(new HashSet<>());
		}

		visit.setName(detail.getName());
		visit.setVisitDate(detail.getVisitDate());
		visit.setClinicalDiagnoses(detail.getClinicalDiagnoses());
		visit.setClinicalStatus(detail.getClinicalStatus());
		visit.setStatus(detail.getStatus());
		visit.setSurgicalPathologyNumber(detail.getSurgicalPathologyNumber());
		visit.setComments(detail.getComments());
		visit.setMissedReason(detail.getMissedReason());
		visit.setEventLabel(detail.getEventLabel());
		visit.setCollectionSite(detail.getSite());
		visit.setCohort(detail.getCohort());
		visit.setActivityStatus(detail.getActivityStatus());
		visit.setAdditionalInfo(detail.getAdditionalInfo());

		StagedParticipant participant = null;
		Long stagedParticipantId = detail.getStagedParticipantId();
		if (stagedParticipantId == null) {
			throw OpenSpecimenException.userError(ParticipantErrorCode.STAGED_ID_REQ);
		} else {
			participant = daoFactory.getStagedParticipantDao().getById(stagedParticipantId);
			if (participant == null) {
				throw OpenSpecimenException.userError(ParticipantErrorCode.STAGED_NOT_FOUND, stagedParticipantId);
			}
		}

		visit.setParticipant(participant);
		visit.setUpdateTime(Calendar.getInstance().getTime());
	}
}
