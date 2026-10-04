package com.krishagni.catissueplus.core.biospecimen.matching;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.StagedParticipant;
import com.krishagni.catissueplus.core.biospecimen.domain.StagedVisit;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.MatchedVisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.StagedParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.StagedVisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSearchDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.Pair;

public class StagedVisitsDbLookup implements VisitsLookup {
	private DaoFactory daoFactory;

	private VisitsLookup osVisitsLookup;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setOsVisitsLookup(VisitsLookup osVisitsLookup) {
		this.osVisitsLookup = osVisitsLookup;
	}

	@Override
	public List<MatchedVisitDetail> getVisits(VisitSearchDetail input) {
		List<MatchedVisitDetail> osVisits = osVisitsLookup.getVisits(input);
		List<StagedVisit> stagedVisits = getStagedVisits(input);
		return mergeVisits(input.getCpId(), osVisits, stagedVisits);
	}

	private List<StagedVisit> getStagedVisits(VisitSearchDetail input) {
		List<StagedVisit> result = new ArrayList<>();

		switch (input.getAttr()) {
			case EMPI_MRN:
				result.addAll(daoFactory.getStagedVisitDao().getByEmpiOrMrn(input.getValue()));
				break;

			case VISIT_NAME:
				StagedVisit visit1 = daoFactory.getStagedVisitDao().getByName(input.getValue());
				if (visit1 != null) {
					result.add(visit1);
				}
				break;

			case SPR_NO:
				StagedVisit visit2 = daoFactory.getStagedVisitDao().getBySprNo(input.getValue());
				if (visit2 != null) {
					result.add(visit2);
				}
				break;
		}

		return result;
	}

	private List<MatchedVisitDetail> mergeVisits(Long cpId, List<MatchedVisitDetail> osVisits, Collection<StagedVisit> stagedVisits) {
		Set<String> osVisitAccNos = new HashSet<>();
		List<MatchedVisitDetail> results = new ArrayList<>(osVisits);
		Map<Long, MatchedVisitDetail> regVisitsMap = new LinkedHashMap<>();
		for (MatchedVisitDetail osVisit : osVisits) {
			regVisitsMap.put(osVisit.getCpr().getId(), osVisit);
			osVisitAccNos.addAll(osVisit.getVisits().stream()
				.map(VisitDetail::getSurgicalPathologyNumber)
				.filter(StringUtils::isNotBlank)
				.collect(Collectors.toSet()));
		}

		Map<String, CollectionProtocolRegistrationDetail> regsMap = new LinkedHashMap<>();
		Map<String, List<VisitDetail>> visitsMap = new LinkedHashMap<>();
		for (StagedVisit stagedVisit : stagedVisits) {
			if (osVisitAccNos.contains(stagedVisit.getSurgicalPathologyNumber())) {
				continue;
			}

			Pair<CollectionProtocolRegistrationDetail, VisitDetail> visitDetail = getVisitDetail(cpId, stagedVisit);
			Long cprId = visitDetail.first().getId();
			if (cprId != null) {
				MatchedVisitDetail regVisits = regVisitsMap.get(cprId);
				if (regVisits != null) {
					regVisits.getVisits().add(visitDetail.second());
					continue;
				}
			}

			String empi = stagedVisit.getParticipant().getEmpi();
			CollectionProtocolRegistrationDetail cpr = regsMap.computeIfAbsent(empi, (u) -> visitDetail.first());
			if (cpr != visitDetail.first()) {
				Date existingDt = cpr.getRegistrationDate();
				Date newDt = visitDetail.first().getRegistrationDate();
				if (existingDt != null && newDt != null && existingDt.after(newDt)) {
					cpr.setRegistrationDate(newDt);
				}
			}

			List<VisitDetail> visits = visitsMap.computeIfAbsent(empi, (u) -> new ArrayList<>());
			visits.add(visitDetail.second());
		}

		regsMap.forEach((empi, reg) -> {
			MatchedVisitDetail match = new MatchedVisitDetail();
			match.setCpr(reg);
			match.setVisits(visitsMap.get(empi));
			results.add(match);
		});

		return results;
	}

	private Pair<CollectionProtocolRegistrationDetail, VisitDetail> getVisitDetail(Long cpId, StagedVisit stagedVisit) {
		StagedParticipant sp = stagedVisit.getParticipant();
		Participant osParticipant = daoFactory.getParticipantDao().getByEmpi(sp.getEmpi());
		CollectionProtocolRegistration cpr = null;
		if (osParticipant != null) {
			cpr = daoFactory.getCprDao().getCprByParticipantId(cpId, osParticipant.getId());
		}

		return getVisitDetail(cpId, cpr, osParticipant, stagedVisit);
	}

	private Pair<CollectionProtocolRegistrationDetail, VisitDetail> getVisitDetail(Long cpId, CollectionProtocolRegistration cpr, Participant participant, StagedVisit visit) {
		CollectionProtocolRegistrationDetail cprDetail;
		if (cpr != null) {
			cprDetail = CollectionProtocolRegistrationDetail.from(cpr, false);
		} else {
			cprDetail = new CollectionProtocolRegistrationDetail();
			cprDetail.setCpId(cpId);
			cprDetail.setRegistrationDate(visit.getVisitDate());

			if (participant != null) {
				cprDetail.setParticipant(ParticipantDetail.from(participant, false));
			} else {
				cprDetail.setParticipant(StagedParticipantDetail.from(visit.getParticipant()));
			}
		}

		return Pair.make(cprDetail, StagedVisitDetail.from(visit));
	}
}
