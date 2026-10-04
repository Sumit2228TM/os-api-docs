package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.AutoFreezerProvider;
import com.krishagni.catissueplus.core.administrative.domain.AutoFreezerProviderDetail;
import com.krishagni.catissueplus.core.administrative.domain.factory.AutoFreezerProviderFactory;
import com.krishagni.catissueplus.core.administrative.events.AutoFreezerProviderErrorCode;
import com.krishagni.catissueplus.core.administrative.services.AutoFreezerProviderService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AutoFreezerProviderServiceImpl implements AutoFreezerProviderService {

	private DaoFactory daoFactory;

	private AutoFreezerProviderFactory providerFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setProviderFactory(AutoFreezerProviderFactory providerFactory) {
		this.providerFactory = providerFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<AutoFreezerProviderDetail>> getProviders() {
		try {
			List<AutoFreezerProvider> providers = daoFactory.getAutoFreezerProviderDao().getAutomatedFreezers();
			return ResponseEvent.response(AutoFreezerProviderDetail.from(providers));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<AutoFreezerProviderDetail> getProvider(RequestEvent<EntityQueryCriteria> req) {
		try {
			EntityQueryCriteria crit = req.getPayload();
			return ResponseEvent.response(AutoFreezerProviderDetail.from(getProvider(crit.getId(), crit.getName())));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<AutoFreezerProviderDetail> registerProvider(RequestEvent<AutoFreezerProviderDetail> req) {
		try {
			AutoFreezerProvider provider = providerFactory.createProvider(req.getPayload());
			ensureUniqueName(provider.getName());
			daoFactory.getAutoFreezerProviderDao().saveOrUpdate(provider);
			return ResponseEvent.response(AutoFreezerProviderDetail.from(provider));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<AutoFreezerProviderDetail> updateProvider(RequestEvent<AutoFreezerProviderDetail> req) {
		try {
			AutoFreezerProviderDetail input = req.getPayload();
			AutoFreezerProvider existing = getProvider(input.getId(), input.getName());
			AutoFreezerProvider provider = providerFactory.createProvider(input);
			if (!provider.getName().equals(existing.getName())) {
				ensureUniqueName(provider.getName());
			}

			existing.update(provider);
			return ResponseEvent.response(AutoFreezerProviderDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private void ensureUniqueName(String name) {
		if (daoFactory.getAutoFreezerProviderDao().getByName(name) != null) {
			throw OpenSpecimenException.userError(AutoFreezerProviderErrorCode.DUP_NAME, name);
		}
	}

	private AutoFreezerProvider getProvider(Long id, String name) {
		Object key = null;
		AutoFreezerProvider provider = null;

		if (id != null) {
			provider = daoFactory.getAutoFreezerProviderDao().getById(id);
			key = id;
		} else if (StringUtils.isNotBlank(name)) {
			provider = daoFactory.getAutoFreezerProviderDao().getByName(name);
			key = name;
		}

		if (key == null) {
			throw OpenSpecimenException.userError(AutoFreezerProviderErrorCode.NAME_REQ);
		} else if (provider == null) {
			throw OpenSpecimenException.userError(AutoFreezerProviderErrorCode.NOT_FOUND, key);
		}

		return provider;
	}
}
