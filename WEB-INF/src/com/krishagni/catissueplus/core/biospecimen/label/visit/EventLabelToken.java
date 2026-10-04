package com.krishagni.catissueplus.core.biospecimen.label.visit;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;

public class EventLabelToken extends AbstractVisitLabelToken {

	public EventLabelToken() {
		this.name = "EVENT_LABEL";
	}

	@Override
	public String getLabel(Visit visit, String... args) {
		CollectionProtocolEvent cpe = visit.getCpEvent();
		return cpe != null && StringUtils.isNotBlank(cpe.getEventLabel()) ? cpe.getEventLabel() : StringUtils.EMPTY;
	}
}
