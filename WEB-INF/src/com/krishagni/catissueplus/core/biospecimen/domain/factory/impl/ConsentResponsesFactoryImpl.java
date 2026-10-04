package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.PvAttributes.CONSENT_RESPONSE;
import static com.krishagni.catissueplus.core.common.service.PvValidator.isValid;

import java.util.HashSet;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentResponses;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTierResponse;
import com.krishagni.catissueplus.core.biospecimen.domain.CpConsentTier;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ConsentResponsesFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ConsentStatementErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CprErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierResponseDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class ConsentResponsesFactoryImpl implements ConsentResponsesFactory {
	
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public ConsentResponses createConsentResponses(CollectionProtocolRegistration cpr, ConsentDetail detail) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		ConsentResponses consentResponses = new ConsentResponses();
		setConsentSignDate(detail, cpr, consentResponses);
		setConsentWitness(detail, cpr, consentResponses, ose);
		setConsentComments(detail, cpr, consentResponses);
		setConsentResponses(detail, cpr, consentResponses, ose);

		ose.checkAndThrow();
		return consentResponses;
	}
	
	private void setConsentSignDate(ConsentDetail detail, CollectionProtocolRegistration cpr, ConsentResponses consentResponses) {
		if (detail.isAttrModified("consentSignatureDate")) {
			consentResponses.setConsentSignDate(detail.getConsentSignatureDate());
		} else {
			consentResponses.setConsentSignDate(cpr.getConsentSignDate());
		}
	}
	
	private void setConsentWitness(ConsentDetail detail, ConsentResponses consentResponses, OpenSpecimenException ose) {
		UserSummary user = detail.getWitness();
		if (user == null) {
			return;
		}
		
		User witness = null;
		if (user.getId() != null) {
			witness = daoFactory.getUserDao().getById(user.getId());
		} else if (user.getEmailAddress() != null) {
			witness = daoFactory.getUserDao().getUserByEmailAddress(user.getEmailAddress());
		} else if (user.getLoginName() != null && user.getDomain() != null) {
			witness = daoFactory.getUserDao().getUser(user.getLoginName(), user.getDomain());
		}
		
		if (witness == null) {
			ose.addError(CprErrorCode.CONSENT_WITNESS_NOT_FOUND);
		}
		
		consentResponses.setConsentWitness(witness);
	}
	
	private void setConsentWitness(
			ConsentDetail detail,
			CollectionProtocolRegistration cpr,
			ConsentResponses consentResponses,
			OpenSpecimenException ose) {

		if (detail.isAttrModified("witness")) {
			setConsentWitness(detail, consentResponses, ose);
		} else {
			consentResponses.setConsentWitness(cpr.getConsentWitness());
		}
	}
	
	private void setConsentComments(ConsentDetail detail, CollectionProtocolRegistration cpr, ConsentResponses consentResponses) {
		if (detail.isAttrModified("comments")) {
			consentResponses.setConsentComments(detail.getComments());
		} else {
			consentResponses.setConsentComments(cpr.getConsentComments());
		}
	}

	private void setConsentResponses(
			ConsentDetail detail,
			CollectionProtocolRegistration cpr,
			ConsentResponses consentResponses,
			OpenSpecimenException ose) {

		Map<String, ConsentTierResponse> responsesMap = cpr.getConsentResponses().stream()
				.collect(Collectors.toMap(ConsentTierResponse::getStatementCode, ConsentTierResponse::copy));

		for (ConsentTierResponseDetail responseDetail : detail.getResponses()) {
			ConsentTierResponse response = createResponse(responseDetail, cpr, responsesMap.get(responseDetail.getStatement()), ose);
			if (response != null) {
				responsesMap.put(response.getStatementCode(), response);
			}
		}

		consentResponses.setConsentResponses(new HashSet<>(responsesMap.values()));
	}

	private ConsentTierResponse createResponse(
			ConsentTierResponseDetail detail,
			CollectionProtocolRegistration cpr,
			ConsentTierResponse response,
			OpenSpecimenException ose) {

		if (!detail.isAttrModified("response")) {
			return null;
		}

		if (response == null) {
			response = new ConsentTierResponse();
			response.setCpr(cpr);

			String key = null;
			Predicate<CpConsentTier> findFn = null;
			if (StringUtils.isNotBlank(detail.getCode())) {
				key = detail.getCode();
				findFn = (t) -> t.getStatement().getCode().equals(detail.getCode());
			} else if (StringUtils.isNotBlank(detail.getStatement())) {
				key = detail.getStatement();
				findFn = (t) -> t.getStatement().getStatement().equals(detail.getStatement());
			}

			if (key == null) {
				ose.addError(ConsentStatementErrorCode.CODE_REQUIRED);
				return null;
			}

			CpConsentTier tier = cpr.getCollectionProtocol().getConsentTier().stream()
				.filter(findFn).findFirst().orElse(null);

			if (tier == null) {
				ose.addError(CprErrorCode.INVALID_CONSENT_STATEMENT, key);
				return null;
			}

			response.setConsentTier(tier);
		}


		String respStr = detail.getResponse();
		PermissibleValue respPv = null;
		if (StringUtils.isNotBlank(respStr)) {
			respPv = daoFactory.getPermissibleValueDao().getPv(CONSENT_RESPONSE, respStr);
			if (respPv == null) {
				ose.addError(CprErrorCode.INVALID_CONSENT_RESPONSE, respStr);
			}
		}

		response.setResponse(respPv);
		return response;
	}
}
