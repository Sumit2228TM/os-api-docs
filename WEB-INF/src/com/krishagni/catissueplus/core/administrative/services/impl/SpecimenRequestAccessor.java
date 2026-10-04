package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.SpecimenRequest;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.service.ObjectAccessor;

public class SpecimenRequestAccessor implements ObjectAccessor {
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public String getObjectName() {
		return SpecimenRequest.getEntityName();
	}

	@Override
	@PlusTransactional
	public Map<String, Object> resolveUrl(String key, Object value) {
		if (key.equals("id")) {
			value = Long.valueOf(value.toString());
		}

		return daoFactory.getSpecimenRequestDao().getRequestIds(key, value);
	}

	@Override
	public String getAuditTable() {
		return "OS_SPECIMEN_REQUESTS_AUD";
	}

	@Override
	public void ensureReadAllowed(Long objectId) {
		// TODO: Implement later.
	}
}
