package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.ContainerActivityLog;
import com.krishagni.catissueplus.core.administrative.events.ContainerActivityLogDetail;

public interface ContainerActivityLogFactory {
	ContainerActivityLog createLog(ContainerActivityLogDetail input);
}
