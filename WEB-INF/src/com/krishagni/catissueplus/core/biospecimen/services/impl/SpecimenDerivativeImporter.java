package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.services.impl.ExtensionsUtil;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class SpecimenDerivativeImporter implements ObjectImporter<SpecimenDetail, SpecimenDetail> {
	
	private SpecimenService specimenSvc;
	
	public void setSpecimenSvc(SpecimenService specimenSvc) {
		this.specimenSvc = specimenSvc;
	}
	
	@Override
	public ResponseEvent<SpecimenDetail> importObject(RequestEvent<ImportObjectDetail<SpecimenDetail>> req) {
		try {
			ImportObjectDetail<SpecimenDetail> detail = req.getPayload();
			SpecimenDetail spmn = detail.getObject();
			ExtensionsUtil.initFileFields(detail.getUploadedFilesDir(), spmn.getExtensionDetail());
			return specimenSvc.createDerivative(RequestEvent.wrap(spmn));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}		
	}
}
