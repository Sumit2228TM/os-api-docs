package com.krishagni.catissueplus.core.common.repository.impl;

import com.krishagni.catissueplus.core.common.domain.PdeAuditLog;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.repository.PdeAuditLogDao;

public class PdeAuditLogDaoImpl extends AbstractDao<PdeAuditLog> implements PdeAuditLogDao {
	public Class<PdeAuditLog> getType() {
		return PdeAuditLog.class;
	}
}
