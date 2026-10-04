package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.ConsentStatement;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ConsentStatementErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ConsentStatementFactory;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentStatementDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.ConsentStatementListCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.ConsentStatementService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class ConsentStatementServiceImpl implements ConsentStatementService {
	private DaoFactory daoFactory;
	
	private ConsentStatementFactory consentStatementFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setConsentStatementFactory(ConsentStatementFactory consentStatementFactory) {
		this.consentStatementFactory = consentStatementFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<ConsentStatementDetail>> getStatements(RequestEvent<ConsentStatementListCriteria> req) {
		try {
			List<ConsentStatement> consents = daoFactory.getConsentStatementDao().getStatements(req.getPayload());
			return ResponseEvent.response(ConsentStatementDetail.from(consents));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<Long> getStatementsCount(RequestEvent<ConsentStatementListCriteria> req) {
		try {
			ConsentStatementListCriteria listCrit = req.getPayload();
			return ResponseEvent.response(daoFactory.getConsentStatementDao().getStatementsCount(listCrit));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ConsentStatementDetail> getStatement(RequestEvent<EntityQueryCriteria> req) {
		try {
			EntityQueryCriteria crit = req.getPayload();
			ConsentStatement stmt = getStatement(crit.getId(), crit.getName());
			return ResponseEvent.response(ConsentStatementDetail.from(stmt));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ConsentStatementDetail> createStatement(RequestEvent<ConsentStatementDetail> req) {
		try {
			if (!AuthUtil.isAdmin() && !AuthUtil.isInstituteAdmin()) {
				return ResponseEvent.userError(RbacErrorCode.ADMIN_RIGHTS_REQUIRED);
			}

			ConsentStatement stmt = consentStatementFactory.createStatement(req.getPayload());
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueStatement(null, stmt, ose);
			ose.checkAndThrow();

			daoFactory.getConsentStatementDao().saveOrUpdate(stmt);
			return ResponseEvent.response(ConsentStatementDetail.from(stmt));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ConsentStatementDetail> updateStatement(RequestEvent<ConsentStatementDetail> req) {
		try {
			if (!AuthUtil.isAdmin() && !AuthUtil.isInstituteAdmin()) {
				return ResponseEvent.userError(RbacErrorCode.ADMIN_RIGHTS_REQUIRED);
			}
			
			ConsentStatementDetail input = req.getPayload();
			ConsentStatement existing = getStatement(input.getId(), input.getCode());
			ConsentStatement consent = consentStatementFactory.createStatement(input);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueStatement(existing, consent, ose);
			ose.checkAndThrow();
			
			existing.update(consent);
			daoFactory.getConsentStatementDao().saveOrUpdate(existing);
			return ResponseEvent.response(ConsentStatementDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private void ensureUniqueStatement(ConsentStatement existingStmt, ConsentStatement newStmt, OpenSpecimenException ose) {
		if (!isUniqueCode(existingStmt, newStmt)) {
			ose.addError(ConsentStatementErrorCode.DUP_CODE, newStmt.getCode());
		}

		if (!isUniqueStatement(existingStmt, newStmt)) {
			ose.addError(ConsentStatementErrorCode.DUP);
		}
	}
	
	private boolean isUniqueCode(ConsentStatement existingStmt, ConsentStatement newStmt) {
		if (existingStmt != null && existingStmt.getCode().equals(newStmt.getCode())) {
			return true;
		}
		
		return daoFactory.getConsentStatementDao().getByCode(newStmt.getCode()) == null;
	}
	
	private boolean isUniqueStatement(ConsentStatement existingStmt, ConsentStatement newStmt) {
		if (existingStmt != null && existingStmt.getStatement().equals(newStmt.getStatement())) {
			return true;
		}

		return daoFactory.getConsentStatementDao().getByStatement(newStmt.getStatement()) == null;
	}

	private ConsentStatement getStatement(Long id, String code) {
		ConsentStatement result = null;
		Object key = null;

		if (id != null) {
			key = id;
			result = daoFactory.getConsentStatementDao().getById(id);
		} else if (StringUtils.isNotBlank(code)) {
			key = code;
			result = daoFactory.getConsentStatementDao().getByCode(code);
		}

		if (key == null) {
			throw OpenSpecimenException.userError(ConsentStatementErrorCode.CODE_REQUIRED);
		} else if (result == null) {
			throw OpenSpecimenException.userError(ConsentStatementErrorCode.NOT_FOUND, key);
		}

		return result;
	}
}
