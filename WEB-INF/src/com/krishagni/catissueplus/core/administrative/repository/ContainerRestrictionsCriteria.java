package com.krishagni.catissueplus.core.administrative.repository;

import java.util.Set;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;
import com.krishagni.catissueplus.core.common.util.Utility;

public class ContainerRestrictionsCriteria extends AbstractListCriteria<ContainerRestrictionsCriteria> {

	private Long containerId;

	private Set<PermissibleValue> spmnClasses;

	private Set<PermissibleValue> spmnTypes;

	private Set<CollectionProtocol> cps;

	private Site site;

	private Set<DistributionProtocol> dps;

	@Override
	public ContainerRestrictionsCriteria self() {
		return this;
	}

	public Long containerId() {
		return containerId;
	}

	public ContainerRestrictionsCriteria containerId(Long containerId) {
		this.containerId = containerId;
		return self();
	}

	public Set<PermissibleValue> specimenClasses() {
		return spmnClasses;
	}

	public ContainerRestrictionsCriteria specimenClasses(Set<PermissibleValue> spmnClasses) {
		this.spmnClasses = spmnClasses;
		return self();
	}

	public Set<PermissibleValue> specimenTypes() {
		return spmnTypes;
	}

	public ContainerRestrictionsCriteria specimenTypes(Set<PermissibleValue> spmnTypes) {
		this.spmnTypes = spmnTypes;
		return self();
	}

	public Set<CollectionProtocol> collectionProtocols() {
		return cps;
	}

	public Set<Long> collectionProtocolIds() {
		return Utility.nullSafeStream(cps).map(CollectionProtocol::getId).collect(Collectors.toSet());
	}

	public ContainerRestrictionsCriteria collectionProtocols(Set<CollectionProtocol> cps) {
		this.cps = cps;
		return self();
	}

	public Site site() {
		return site;
	}

	public Long siteId() {
		return site != null ? site.getId() : null;
	}

	public ContainerRestrictionsCriteria site(Site site) {
		this.site = site;
		return self();
	}

	public Set<DistributionProtocol> distributionProtocols() {
		return dps;
	}

	public Set<Long> distributionProtocolIds() {
		return Utility.nullSafeStream(dps).map(DistributionProtocol::getId).collect(Collectors.toSet());
	}

	public ContainerRestrictionsCriteria distributionProtocols(Set<DistributionProtocol> dps) {
		this.dps = dps;
		return self();
	}
}
