package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.events.ContainerTypeDetail;
import com.krishagni.catissueplus.core.administrative.services.ContainerTypeService;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class ContainerTypeImporter implements ObjectImporter<ContainerTypeDetail, ContainerTypeDetail> {

	private ContainerTypeService containerTypeSvc;

	public void setContainerTypeSvc(ContainerTypeService containerTypeSvc) {
		this.containerTypeSvc = containerTypeSvc;
	}

	@Override
	public ResponseEvent<ContainerTypeDetail> importObject(RequestEvent<ImportObjectDetail<ContainerTypeDetail>> req) {
		try {
			ImportObjectDetail<ContainerTypeDetail> detail = req.getPayload();
			RequestEvent<ContainerTypeDetail> typeReq = new RequestEvent<>(detail.getObject());
			if (detail.isCreate()) {
				return containerTypeSvc.createContainerType(typeReq);
			} else {
				return containerTypeSvc.patchContainerType(typeReq);
			}
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}
