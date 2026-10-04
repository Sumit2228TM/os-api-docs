package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;

public class MatchedVisitDetail {
	private CollectionProtocolRegistrationDetail cpr;

	private List<VisitDetail> visits;

	public CollectionProtocolRegistrationDetail getCpr() {
		return cpr;
	}

	public void setCpr(CollectionProtocolRegistrationDetail cpr) {
		this.cpr = cpr;
	}

	public List<VisitDetail> getVisits() {
		return visits;
	}

	public void setVisits(List<VisitDetail> visits) {
		this.visits = visits;
	}

	public static MatchedVisitDetail from(CollectionProtocolRegistration cpr, List<Visit> visits) {
		MatchedVisitDetail detail = new MatchedVisitDetail();
		detail.setCpr(CollectionProtocolRegistrationDetail.from(cpr, false));
		detail.setVisits(visits.stream().map(v -> VisitDetail.from(v, false, false)).collect(Collectors.toList()));
		return detail;
	}
}
