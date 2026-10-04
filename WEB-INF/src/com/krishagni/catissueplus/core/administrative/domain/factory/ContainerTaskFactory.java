package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.ContainerTask;
import com.krishagni.catissueplus.core.administrative.events.ContainerTaskDetail;

public interface ContainerTaskFactory {
	ContainerTask createTask(ContainerTaskDetail input);
}
