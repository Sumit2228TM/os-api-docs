package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolEventDetail;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class CpeImporter implements ObjectImporter<CollectionProtocolEventDetail, CollectionProtocolEventDetail> {
	private CollectionProtocolService cpSvc;

	public void setCpSvc(CollectionProtocolService cpSvc) {
		this.cpSvc = cpSvc;
	}

	@Override
	public ResponseEvent<CollectionProtocolEventDetail> importObject(RequestEvent<ImportObjectDetail<CollectionProtocolEventDetail>> req) {
		try {
			ImportObjectDetail<CollectionProtocolEventDetail> detail = req.getPayload();
			if (detail.isCreate()) {
				return cpSvc.addEvent(RequestEvent.wrap(detail.getObject()));
			} else {
				return cpSvc.updateEvent(RequestEvent.wrap(detail.getObject()));
			}
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}