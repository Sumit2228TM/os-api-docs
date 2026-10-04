package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.List;

public class MatchedParticipantsList {
	private ParticipantDetail criteria;

	private List<MatchedParticipant> matches = new ArrayList<>();

	public ParticipantDetail getCriteria() {
		return criteria;
	}

	public void setCriteria(ParticipantDetail criteria) {
		this.criteria = criteria;
	}

	public List<MatchedParticipant> getMatches() {
		return matches;
	}

	public void setMatches(List<MatchedParticipant> matches) {
		this.matches = matches;
	}

	public static MatchedParticipantsList from(ParticipantDetail criteria, List<MatchedParticipant> matches) {
		MatchedParticipantsList result = new MatchedParticipantsList();
		result.setCriteria(criteria);
		result.setMatches(matches);
		return result;
	}
}
