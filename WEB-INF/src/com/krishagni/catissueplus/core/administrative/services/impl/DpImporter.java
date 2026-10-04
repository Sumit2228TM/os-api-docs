package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
import com.krishagni.catissueplus.core.administrative.services.DistributionProtocolService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.services.impl.ExtensionsUtil;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class DpImporter implements ObjectImporter<DistributionProtocolDetail, DistributionProtocolDetail> {
	private DistributionProtocolService dpSvc;

	public void setDpSvc(DistributionProtocolService dpSvc) {
		this.dpSvc = dpSvc;
	}

	@Override
	public ResponseEvent<DistributionProtocolDetail> importObject(RequestEvent<ImportObjectDetail<DistributionProtocolDetail>> req) {
		try {
			ImportObjectDetail<DistributionProtocolDetail> detail = req.getPayload();
			DistributionProtocolDetail dp = detail.getObject();
			ExtensionsUtil.initFileFields(detail.getUploadedFilesDir(), dp.getExtensionDetail());

			if (detail.isCreate()) {
				return dpSvc.createDistributionProtocol(RequestEvent.wrap(dp));
			} else {
				return dpSvc.patchDistributionProtocol(RequestEvent.wrap(dp));
			}
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}
