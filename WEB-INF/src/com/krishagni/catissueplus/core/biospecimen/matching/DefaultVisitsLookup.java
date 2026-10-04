package com.krishagni.catissueplus.core.biospecimen.matching;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.events.MatchedVisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSearchDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class DefaultVisitsLookup implements VisitsLookup {
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public List<MatchedVisitDetail> getVisits(VisitSearchDetail input) {
		List<Visit> visits = new ArrayList<>();

		switch (input.getAttr()) {
			case EMPI_MRN:
				visits.addAll(daoFactory.getVisitsDao().getByEmpiOrMrn(input.getCpId(), input.getValue()));
				break;

			case VISIT_NAME:
				Visit visit1 = daoFactory.getVisitsDao().getByName(input.getValue());
				if (visit1 != null) {
					visits.add(visit1);
				}
				break;

			case SPR_NO:
				visits.addAll(daoFactory.getVisitsDao().getBySpr(input.getCpId(), input.getValue()));
				break;
		}

		Map<CollectionProtocolRegistration, List<Visit>> regVisitsMap = new LinkedHashMap<>();
		for (Visit visit : visits) {
			List<Visit> regVisits = regVisitsMap.computeIfAbsent(visit.getRegistration(), (k) -> new ArrayList<>());
			regVisits.add(visit);
		}

		return regVisitsMap.entrySet().stream()
			.map(regVisits -> MatchedVisitDetail.from(regVisits.getKey(), regVisits.getValue()))
			.collect(Collectors.toList());
	}
}
