package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.domain.AbstractGreekLetterSequenceToken;

public class ParentSpecimenGreekLetterSequence extends AbstractGreekLetterSequenceToken<Specimen> {

	public ParentSpecimenGreekLetterSequence() {
		this.name = "PSPEC_GREEK_SEQ";
	}

	@Override
	protected Integer getSequence(Specimen spmn) {
		String id = null;
		if (spmn.getParentSpecimen() != null) {
			if (spmn.getCollectionProtocol().useLabelsAsSequenceKey()) {
				id = spmn.getCpId() + "_" + spmn.getParentSpecimen().getLabel();
			} else {
				id = spmn.getParentSpecimen().getId().toString();
			}
		}

		return getUniqueId(id);
	}
}