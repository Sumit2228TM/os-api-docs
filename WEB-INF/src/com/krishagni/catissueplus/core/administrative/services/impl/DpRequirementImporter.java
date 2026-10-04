package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.events.DpRequirementDetail;
import com.krishagni.catissueplus.core.administrative.services.DistributionProtocolService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.services.impl.ExtensionsUtil;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class DpRequirementImporter implements ObjectImporter<DpRequirementDetail, DpRequirementDetail> {
	private DistributionProtocolService dpSvc;

	public void setDpSvc(DistributionProtocolService dpSvc) {
		this.dpSvc = dpSvc;
	}

	@Override
	public ResponseEvent<DpRequirementDetail> importObject(RequestEvent<ImportObjectDetail<DpRequirementDetail>> req) {
		try {
			ImportObjectDetail<DpRequirementDetail> detail = req.getPayload();
			DpRequirementDetail dpReq = detail.getObject();
			ExtensionsUtil.initFileFields(detail.getUploadedFilesDir(), dpReq.getExtensionDetail());

			if (detail.isCreate()) {
				return dpSvc.createRequirement(RequestEvent.wrap(dpReq));
			} else {
				return dpSvc.patchRequirement(RequestEvent.wrap(dpReq));
			}
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}
