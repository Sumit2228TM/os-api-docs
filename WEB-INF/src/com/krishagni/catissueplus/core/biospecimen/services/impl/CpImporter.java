package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.services.impl.ExtensionsUtil;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class CpImporter implements ObjectImporter<CollectionProtocolDetail, CollectionProtocolDetail> {
	private CollectionProtocolService cpSvc;

	public void setCpSvc(CollectionProtocolService cpSvc) {
		this.cpSvc = cpSvc;
	}

	@Override
	public ResponseEvent<CollectionProtocolDetail> importObject(RequestEvent<ImportObjectDetail<CollectionProtocolDetail>> req) {
		try {
			ImportObjectDetail<CollectionProtocolDetail> detail = req.getPayload();
			CollectionProtocolDetail cp = detail.getObject();
			ExtensionsUtil.initFileFields(detail.getUploadedFilesDir(), cp.getExtensionDetail());
			
			if (detail.isCreate()) {
				return cpSvc.createCollectionProtocol(RequestEvent.wrap(cp));
			} else {
				return cpSvc.updateCollectionProtocol(RequestEvent.wrap(cp));
			}
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}