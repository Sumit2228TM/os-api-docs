package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.AutoFreezerProvider;
import com.krishagni.catissueplus.core.administrative.domain.AutoFreezerProviderDetail;
import com.krishagni.catissueplus.core.administrative.domain.factory.AutoFreezerProviderFactory;
import com.krishagni.catissueplus.core.administrative.events.AutoFreezerProviderErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class AutoFreezerProviderFactoryImpl implements AutoFreezerProviderFactory {

	@Override
	public AutoFreezerProvider createProvider(AutoFreezerProviderDetail detail) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		AutoFreezerProvider provider = new AutoFreezerProvider();
		setName(detail, provider, ose);
		setImplClass(detail, provider, ose);
		setProps(detail, provider, ose);

		ose.checkAndThrow();
		return provider;
	}

	public void setName(AutoFreezerProviderDetail detail, AutoFreezerProvider provider, OpenSpecimenException ose) {
		if (StringUtils.isBlank(detail.getName())) {
			ose.addError(AutoFreezerProviderErrorCode.NAME_REQ);
		}

		provider.setName(detail.getName());
	}

	public void setImplClass(AutoFreezerProviderDetail detail, AutoFreezerProvider provider, OpenSpecimenException ose) {
		if (StringUtils.isBlank(detail.getImplClass())) {
			ose.addError(AutoFreezerProviderErrorCode.CLASS_REQ);
			return;
		}

		try {
			Class.forName(detail.getImplClass());
			provider.setImplClass(detail.getImplClass());
		} catch (Exception e) {
			ose.addError(AutoFreezerProviderErrorCode.INVALID_CLASS, detail.getImplClass(), e.toString());
		}
	}

	public void setProps(AutoFreezerProviderDetail detail, AutoFreezerProvider provider, OpenSpecimenException ose) {
		if (detail.getProps() != null && !detail.getProps().isEmpty()) {
			provider.setProps(detail.getProps());
		}
	}
}
