package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.AutoFreezerProviderDetail;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface AutoFreezerProviderService {
	ResponseEvent<List<AutoFreezerProviderDetail>> getProviders();

	ResponseEvent<AutoFreezerProviderDetail> getProvider(RequestEvent<EntityQueryCriteria> req);

	ResponseEvent<AutoFreezerProviderDetail> registerProvider(RequestEvent<AutoFreezerProviderDetail> req);

	ResponseEvent<AutoFreezerProviderDetail> updateProvider(RequestEvent<AutoFreezerProviderDetail> req);
}
