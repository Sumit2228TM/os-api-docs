package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;

public interface ContainerDefragmenter {
	int defragment(StorageContainer container);
}
