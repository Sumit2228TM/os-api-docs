
package com.krishagni.catissueplus.core.administrative.services;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.events.DistributionOrderStat;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderStatListCriteria;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
import com.krishagni.catissueplus.core.administrative.events.DpConsentTierDetail;
import com.krishagni.catissueplus.core.administrative.events.DpRequirementDetail;
import com.krishagni.catissueplus.core.administrative.repository.DpListCriteria;
import com.krishagni.catissueplus.core.common.events.BulkDeleteEntityOp;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface DistributionProtocolService {

	public ResponseEvent<List<DistributionProtocolDetail>> getDistributionProtocols(RequestEvent<DpListCriteria> req);
	
	public ResponseEvent<Long> getDistributionProtocolsCount(RequestEvent<DpListCriteria> req);
	
	public ResponseEvent<DistributionProtocolDetail> getDistributionProtocol(RequestEvent<Long> req);
	
	public ResponseEvent<DistributionProtocolDetail> createDistributionProtocol(RequestEvent<DistributionProtocolDetail> req);

	public ResponseEvent<DistributionProtocolDetail> updateDistributionProtocol(RequestEvent<DistributionProtocolDetail> req);

	public ResponseEvent<DistributionProtocolDetail> patchDistributionProtocol(RequestEvent<DistributionProtocolDetail> req);
	
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<Long> req);
	
	public ResponseEvent<List<DistributionProtocolDetail>> deleteDistributionProtocols(RequestEvent<BulkDeleteEntityOp> req);
	
	public ResponseEvent<DistributionProtocolDetail> updateActivityStatus(RequestEvent<DistributionProtocolDetail> req);
	
	public ResponseEvent<List<DistributionOrderStat>> getOrderStats(RequestEvent<DistributionOrderStatListCriteria> req);
	
	public ResponseEvent<List<DpRequirementDetail>> getRequirements(RequestEvent<Long> req);
	
	public ResponseEvent<DpRequirementDetail> getRequirement(RequestEvent<Long> req);
	
	public ResponseEvent<DpRequirementDetail> createRequirement(RequestEvent<DpRequirementDetail> req);
	
	public ResponseEvent<DpRequirementDetail> updateRequirement(RequestEvent<DpRequirementDetail> req);

	public ResponseEvent<DpRequirementDetail> patchRequirement(RequestEvent<DpRequirementDetail> req);
	
	public ResponseEvent<DpRequirementDetail> deleteRequirement(RequestEvent<Long> req);
	
	public ResponseEvent<File> exportOrderStats(RequestEvent<DistributionOrderStatListCriteria> req);

	public ResponseEvent<Map<String, Object>> getExtensionForm();

	ResponseEvent<Map<String, Object>> getOrderExtensionForm(Long dpId);
	
	ResponseEvent<List<DpConsentTierDetail>> getConsentTiers(RequestEvent<EntityQueryCriteria> req);
	
	ResponseEvent<DpConsentTierDetail> createConsentTier(RequestEvent<DpConsentTierDetail> req);

	ResponseEvent<DpConsentTierDetail> updateConsentTier(RequestEvent<DpConsentTierDetail> req);

	ResponseEvent<DpConsentTierDetail> deleteConsentTier(RequestEvent<DpConsentTierDetail> req);

	boolean toggleStarredDp(Long dpId, boolean starred);
}
