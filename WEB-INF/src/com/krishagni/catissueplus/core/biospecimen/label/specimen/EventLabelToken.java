package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class EventLabelToken extends AbstractSpecimenLabelToken {

	public EventLabelToken() {
		this.name = "EVENT_LABEL";
	}

	@Override
	public String getLabel(Specimen specimen) {
		CollectionProtocolEvent cpe = specimen.getVisit().getCpEvent();
		return cpe != null && StringUtils.isNotBlank(cpe.getEventLabel()) ? cpe.getEventLabel() : StringUtils.EMPTY;
	}
}
