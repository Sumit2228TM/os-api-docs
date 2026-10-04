package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.Collections;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.ReturnedSpecimenDetail;
import com.krishagni.catissueplus.core.administrative.services.DistributionOrderService;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class ReturnSpecimensImporter implements ObjectImporter<ReturnedSpecimenDetail, List<SpecimenInfo>> {

	private DistributionOrderService orderSvc;

	public void setOrderSvc(DistributionOrderService orderSvc) {
		this.orderSvc = orderSvc;
	}

	@Override
	public ResponseEvent<List<SpecimenInfo>> importObject(RequestEvent<ImportObjectDetail<ReturnedSpecimenDetail>> req) {
		try {
			return orderSvc.returnSpecimens(request(req.getPayload().getObject()));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private RequestEvent<List<ReturnedSpecimenDetail>> request(ReturnedSpecimenDetail detail) {
		return new RequestEvent<>(Collections.singletonList(detail));
	}
}
