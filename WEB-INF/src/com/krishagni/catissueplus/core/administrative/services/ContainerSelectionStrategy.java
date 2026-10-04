package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.events.ContainerCriteria;

public interface ContainerSelectionStrategy {
	StorageContainer getContainer(ContainerCriteria criteria, Boolean aliquotsInSameContainer);
}
