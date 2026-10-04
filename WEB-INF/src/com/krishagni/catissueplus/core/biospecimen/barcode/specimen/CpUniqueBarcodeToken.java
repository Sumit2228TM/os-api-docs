package com.krishagni.catissueplus.core.biospecimen.barcode.specimen;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.domain.AbstractUniqueIdToken;

public class CpUniqueBarcodeToken extends AbstractUniqueIdToken<Specimen> {

	@Autowired
	private DaoFactory daoFactory;

	public CpUniqueBarcodeToken() {
		this.name = "CP_UID";
	}


	@Override
	public Number getUniqueId(Specimen specimen, String... args) {
		String key = "CP_UID_" + specimen.getCpId().toString();
		return daoFactory.getUniqueIdGenerator().getUniqueId("SPMN_BARCODE", key);
	}
}
