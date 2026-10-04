package com.krishagni.catissueplus.core.administrative.print.container;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public class TypePrintToken extends AbstractLabelTmplToken implements LabelTmplToken {
	@Override
	public String getName() {
		return "container_type";
	}

	@Override
	public String getReplacement(Object object) {
		StorageContainer container = (StorageContainer) object;
		return container.getType() != null ? container.getType().getName() : StringUtils.EMPTY;
	}
}
