package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.ContainerTypeDetail;
import com.krishagni.catissueplus.core.administrative.events.ContainerTypeSummary;
import com.krishagni.catissueplus.core.administrative.repository.ContainerTypeListCriteria;
import com.krishagni.catissueplus.core.common.events.BulkDeleteEntityOp;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface ContainerTypeService {
	ResponseEvent<List<ContainerTypeSummary>> getContainerTypes(RequestEvent<ContainerTypeListCriteria> req);

	ResponseEvent<Long> getContainerTypesCount(RequestEvent<ContainerTypeListCriteria> req);

	ResponseEvent<ContainerTypeDetail> getContainerType(RequestEvent<EntityQueryCriteria> req);

	ResponseEvent<ContainerTypeDetail> createContainerType(RequestEvent<ContainerTypeDetail> req);
	
	ResponseEvent<ContainerTypeDetail> updateContainerType(RequestEvent<ContainerTypeDetail> req);

	ResponseEvent<ContainerTypeDetail> patchContainerType(RequestEvent<ContainerTypeDetail> req);
	
	ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<Long> req);
	
	ResponseEvent<ContainerTypeDetail> deleteContainerType(RequestEvent<Long> req);

	ResponseEvent<Integer> deleteContainerTypes(RequestEvent<BulkDeleteEntityOp> req);
}
