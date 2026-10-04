package com.krishagni.catissueplus.core.administrative.events;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.administrative.domain.DpRequirement;
import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.common.AttributeModifiedSupport;
import com.krishagni.catissueplus.core.common.ListenAttributeChanges;
import com.krishagni.catissueplus.core.de.events.ExtensionDetail;

@ListenAttributeChanges
public class DpRequirementDetail extends AttributeModifiedSupport {
	private Long id;
	
	private DistributionProtocolSummary dp;
	
	private String specimenType;
	
	private String anatomicSite;
	
	private Set<String> pathologyStatuses;

	private String clinicalDiagnosis;

	private BigDecimal cost;
	
	private Long specimenCount;
	
	private BigDecimal quantity;

	private Long distributedCnt;
	
	private BigDecimal distributedQty;
	
	private String comments;
	
	private String activityStatus;

	private ExtensionDetail extensionDetail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public DistributionProtocolSummary getDp() {
		return dp;
	}
	
	public void setDp(DistributionProtocolSummary dp) {
		this.dp = dp;
	}

	public String getSpecimenType() {
		return specimenType;
	}

	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}

	public String getAnatomicSite() {
		return anatomicSite;
	}

	public void setAnatomicSite(String anatomicSite) {
		this.anatomicSite = anatomicSite;
	}

	public Set<String> getPathologyStatuses() {
		return pathologyStatuses;
	}

	public void setPathologyStatuses(Set<String> pathologyStatuses) {
		this.pathologyStatuses = pathologyStatuses;
	}

	public String getClinicalDiagnosis() {
		return clinicalDiagnosis;
	}

	public void setClinicalDiagnosis(String clinicalDiagnosis) {
		this.clinicalDiagnosis = clinicalDiagnosis;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public Long getSpecimenCount() {
		return specimenCount;
	}

	public void setSpecimenCount(Long specimenCount) {
		this.specimenCount = specimenCount;
	}
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Long getDistributedCnt() {
		return distributedCnt;
	}

	public void setDistributedCnt(Long distributedCnt) {
		this.distributedCnt = distributedCnt;
	}

	public BigDecimal getDistributedQty() {
		return distributedQty;
	}
	
	public void setDistributedQty(BigDecimal distributedQty) {
		this.distributedQty = distributedQty;
	}
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getActivityStatus() {
		return activityStatus;
	}
	
	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public ExtensionDetail getExtensionDetail() {
		return extensionDetail;
	}

	public void setExtensionDetail(ExtensionDetail extensionDetail) {
		this.extensionDetail = extensionDetail;
	}

	public static DpRequirementDetail from(DpRequirement dpr) {
		DpRequirementDetail detail = new DpRequirementDetail();
		
		detail.setId(dpr.getId());
		detail.setDp(DistributionProtocolSummary.from(dpr.getDistributionProtocol()));
		detail.setSpecimenType(PermissibleValue.getValue(dpr.getSpecimenType()));
		detail.setAnatomicSite(PermissibleValue.getValue(dpr.getAnatomicSite()));
		detail.setPathologyStatuses(PermissibleValue.toValueSet(dpr.getPathologyStatuses()));
		detail.setClinicalDiagnosis(PermissibleValue.getValue(dpr.getClinicalDiagnosis()));
		detail.setCost(dpr.getCost());
		detail.setSpecimenCount(dpr.getSpecimenCount());
		detail.setQuantity(dpr.getQuantity());
		detail.setComments(dpr.getComments());
		detail.setActivityStatus(dpr.getActivityStatus());
		detail.setExtensionDetail(ExtensionDetail.from(dpr.getExtension()));
		return detail;
	}
	
	public static List<DpRequirementDetail> from(Collection<DpRequirement> dprs) {
		return dprs.stream().map(DpRequirementDetail::from).collect(Collectors.toList());
	}
}
