package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.common.service.impl.AbstractSearchEntityKeywordProvider;
import com.krishagni.catissueplus.core.common.util.Status;

public class ParticipantSearchKeywordProvider extends AbstractSearchEntityKeywordProvider {
	private static final List<String> PROPS = Arrays.asList("firstName", "lastName", "uid", "empi");

	@Override
	public Set<Long> getEntityIds(Object entity) {
		Participant participant = (Participant) entity;

		Set<Long> result = null;
		if (participant.getCprs().isEmpty() && participant.isDeleted()) {
			result = participant.getOldCprIds();
		} else {
			result = participant.getCprs().stream().map(CollectionProtocolRegistration::getId).collect(Collectors.toSet());
		}

		if (participant.getNewCprIds() != null) {
			result.removeAll(participant.getNewCprIds());
		}

		if (participant.getKwAddedCprIds() != null) {
			result.removeAll(participant.getKwAddedCprIds());
		}

		return result;
	}

	@Override
	public List<String> getKeywordProps() {
		return PROPS;
	}

	@Override
	public String getEntityName() {
		return CollectionProtocolRegistration.getEntityName();
	}

	@Override
	public boolean isEntityDeleted(Object entity) {
		return Status.ACTIVITY_STATUS_DISABLED.equals(((Participant) entity).getActivityStatus());
	}

	@Override
	public String getEntity() {
		return Participant.class.getName();
	}
}
