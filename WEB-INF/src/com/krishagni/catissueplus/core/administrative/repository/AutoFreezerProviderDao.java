package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.AutoFreezerProvider;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface AutoFreezerProviderDao extends Dao<AutoFreezerProvider> {
	List<AutoFreezerProvider> getAutomatedFreezers();

	AutoFreezerProvider getByName(String name);
}
