package com.krishagni.catissueplus.core.common.service;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UpgradeLogDetail;

public interface UpgradeLogService {
	ResponseEvent<List<UpgradeLogDetail>> getUpgradeLogs();
}
