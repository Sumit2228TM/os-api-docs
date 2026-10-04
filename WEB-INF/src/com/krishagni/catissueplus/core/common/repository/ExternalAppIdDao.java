package com.krishagni.catissueplus.core.common.repository;

import com.krishagni.catissueplus.core.common.domain.ExternalAppId;

public interface ExternalAppIdDao extends Dao<ExternalAppId> {
	ExternalAppId getByExternalId(String appName, String entityName, String externalId);
}
