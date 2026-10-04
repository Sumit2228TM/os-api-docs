package com.krishagni.catissueplus.core.biospecimen.print;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public class SpecimenMpiPrintToken extends AbstractLabelTmplToken implements LabelTmplToken {

	@Override
	public String getName() {
		return "specimen_mpi";
	}

	@Override
	public String getReplacement(Object object) {
		Specimen specimen = (Specimen)object;
		String mpi = specimen.getRegistration().getParticipant().getEmpi();
		return StringUtils.isNotBlank(mpi) ? mpi : StringUtils.EMPTY;
	}
}
