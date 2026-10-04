package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;
import com.krishagni.catissueplus.core.administrative.services.DistributionOrderService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.services.impl.ExtensionsUtil;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class DistributionOrderImporter implements ObjectImporter<DistributionOrderDetail, DistributionOrderDetail> {
	private DistributionOrderService orderSvc;

	public void setOrderSvc(DistributionOrderService orderSvc) {
		this.orderSvc = orderSvc;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionOrderDetail> importObject(RequestEvent<ImportObjectDetail<DistributionOrderDetail>> req) {
		try {
			ImportObjectDetail<DistributionOrderDetail> detail = req.getPayload();
			DistributionOrderDetail order = detail.getObject();
			ExtensionsUtil.initFileFields(detail.getUploadedFilesDir(), order.getExtensionDetail());

			if (detail.isCreate()) {
				return orderSvc.createOrder(RequestEvent.wrap(order));
			} else {
				return orderSvc.updateOrder(RequestEvent.wrap(order));
			}
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}
