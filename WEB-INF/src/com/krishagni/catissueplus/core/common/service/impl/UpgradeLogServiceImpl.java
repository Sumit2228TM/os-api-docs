package com.krishagni.catissueplus.core.common.service.impl;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.domain.UpgradeLog;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UpgradeLogDetail;
import com.krishagni.catissueplus.core.common.service.UpgradeLogService;

public class UpgradeLogServiceImpl implements UpgradeLogService {
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<UpgradeLogDetail>> getUpgradeLogs() {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			List<UpgradeLog> logs = daoFactory.getUpgradeLogDao().getUpgradeLogs();
			return ResponseEvent.response(UpgradeLogDetail.from(logs));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}