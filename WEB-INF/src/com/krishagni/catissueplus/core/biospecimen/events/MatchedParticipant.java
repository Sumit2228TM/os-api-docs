package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;

public class MatchedParticipant {	
	private List<String> matchedAttrs;

	private ParticipantDetail participant;

	public MatchedParticipant(ParticipantDetail participant, List<String> matchedAttrs) {
		this.matchedAttrs = matchedAttrs;
		this.participant = participant;
	}
	
	public List<String> getMatchedAttrs() {
		return matchedAttrs;
	}

	public void setMatchedAttrs(List<String> matchedAttrs) {
		this.matchedAttrs = matchedAttrs;
	}

	public ParticipantDetail getParticipant() {
		return participant;
	}
	
	public void setParticipant(ParticipantDetail participant) {
		this.participant = participant;
	}
	
	public static MatchedParticipant from(Participant participant, List<String> matchedAttrs) {
		return new MatchedParticipant(ParticipantDetail.from(participant, false), matchedAttrs);
	}
	
	public static List<MatchedParticipant> from(Map<Participant, List<String>> matchedParticipants) {
		return matchedParticipants.entrySet().stream()
			.map(mp -> MatchedParticipant.from(mp.getKey(), mp.getValue()))
			.collect(Collectors.toList());
	}
}