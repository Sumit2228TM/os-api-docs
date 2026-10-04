package com.krishagni.catissueplus.core.administrative.print.container;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public class NamePrintToken extends AbstractLabelTmplToken implements LabelTmplToken {
	@Override
	public String getName() {
		return "container_name";
	}

	@Override
	public String getReplacement(Object object) {
		StorageContainer container = (StorageContainer) object;
		return container.getName();
	}
}
