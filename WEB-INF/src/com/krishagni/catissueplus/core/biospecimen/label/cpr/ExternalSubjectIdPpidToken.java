package com.krishagni.catissueplus.core.biospecimen.label.cpr;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;

public class ExternalSubjectIdPpidToken extends AbstractPpidToken {
	public ExternalSubjectIdPpidToken() {
		name = "EXT_SUBJECT_ID";
	}

	@Override
	public String getLabel(CollectionProtocolRegistration cpr, String... args) {
		return StringUtils.isBlank(cpr.getExternalSubjectId()) ? EMPTY_VALUE : cpr.getExternalSubjectId();
	}
}
