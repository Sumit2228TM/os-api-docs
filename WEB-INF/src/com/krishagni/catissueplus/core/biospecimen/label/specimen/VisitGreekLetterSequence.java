package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.domain.AbstractGreekLetterSequenceToken;

public class VisitGreekLetterSequence extends AbstractGreekLetterSequenceToken<Specimen> {

	public VisitGreekLetterSequence() {
		this.name = "VISIT_GREEK_SEQ";
	}

	@Override
	protected Integer getSequence(Specimen spmn) {
		String key = null;
		if (spmn.getCollectionProtocol().useLabelsAsSequenceKey()) {
			key = spmn.getCpId() + "_" + spmn.getVisit().getName();
		} else {
			key = spmn.getVisit().getId().toString();
		}

		return getUniqueId(key);
	}
}
