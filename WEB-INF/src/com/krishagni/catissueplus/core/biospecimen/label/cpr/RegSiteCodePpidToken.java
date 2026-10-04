package com.krishagni.catissueplus.core.biospecimen.label.cpr;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolSite;

public class RegSiteCodePpidToken extends AbstractPpidToken {
	public RegSiteCodePpidToken() {
		name = "REG_SITE_CODE";
	}

	@Override
	public String getLabel(CollectionProtocolRegistration cpr, String... args) {
		CollectionProtocolSite cpSite = getCpSite(cpr);
		if (cpSite == null || StringUtils.isBlank(cpSite.getCode())) {
			return EMPTY_VALUE;
		} else {
			return cpSite.getCode();
		}
	}

	private CollectionProtocolSite getCpSite(CollectionProtocolRegistration cpr) {
		if (cpr.getSite() == null) {
			return null;
		}

		return cpr.getCollectionProtocol().getSites().stream()
			.filter(cpSite -> cpSite.getSite().equals(cpr.getSite()))
			.findFirst().orElse(null);
	}
}
