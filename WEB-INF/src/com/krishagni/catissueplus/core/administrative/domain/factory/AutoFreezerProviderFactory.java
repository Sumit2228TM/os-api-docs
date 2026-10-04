package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.AutoFreezerProvider;
import com.krishagni.catissueplus.core.administrative.domain.AutoFreezerProviderDetail;

public interface AutoFreezerProviderFactory {
	AutoFreezerProvider createProvider(AutoFreezerProviderDetail detail);
}
