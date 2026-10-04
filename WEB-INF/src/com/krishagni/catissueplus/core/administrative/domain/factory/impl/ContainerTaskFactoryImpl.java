package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.ContainerTask;
import com.krishagni.catissueplus.core.administrative.domain.factory.ContainerTaskErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.ContainerTaskFactory;
import com.krishagni.catissueplus.core.administrative.events.ContainerTaskDetail;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;

public class ContainerTaskFactoryImpl implements ContainerTaskFactory {
	@Override
	public ContainerTask createTask(ContainerTaskDetail input) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		ContainerTask task = new ContainerTask();
		task.setId(input.getId());
		setName(input, task, ose);
		setDescription(input, task, ose);
		setActivityStatus(input, task, ose);
		ose.checkAndThrow();
		return task;
	}

	private void setName(ContainerTaskDetail input, ContainerTask task, OpenSpecimenException ose) {
		if (StringUtils.isBlank(input.getName())) {
			ose.addError(ContainerTaskErrorCode.NAME_REQ);
			return;
		}

		task.setName(input.getName());
	}

	private void setDescription(ContainerTaskDetail input, ContainerTask task, OpenSpecimenException ose) {
		task.setDescription(input.getDescription());
	}

	private void setActivityStatus(ContainerTaskDetail input, ContainerTask task, OpenSpecimenException ose) {
		if (StringUtils.isBlank(input.getActivityStatus())) {
			task.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
		} else if (Status.isValidActivityStatus(input.getActivityStatus())) {
			task.setActivityStatus(input.getActivityStatus());
		} else {
			ose.addError(ActivityStatusErrorCode.INVALID, input.getActivityStatus());
		}
	}
}
