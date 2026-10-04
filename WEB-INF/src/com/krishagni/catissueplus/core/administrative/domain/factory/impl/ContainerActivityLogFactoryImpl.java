package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.ContainerActivityLog;
import com.krishagni.catissueplus.core.administrative.domain.ContainerTask;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledContainerActivity;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.ContainerActivityErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.ContainerActivityLogFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.ContainerTaskErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.ContainerActivityLogDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;

public class ContainerActivityLogFactoryImpl implements ContainerActivityLogFactory {
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public ContainerActivityLog createLog(ContainerActivityLogDetail input) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		ContainerActivityLog log = new ContainerActivityLog();
		log.setId(input.getId());
		setActivity(input, log, ose);
		setContainer(input, log, ose);
		setTask(input, log, ose);
		setPerformedBy(input, log, ose);
		setActivityDate(input, log, ose);
		setTimeTaken(input, log, ose);
		log.setComments(input.getComments());
		setActivityStatus(input, log, ose);
		ose.checkAndThrow();
		return log;
	}

	private void setActivity(ContainerActivityLogDetail input, ContainerActivityLog log, OpenSpecimenException ose) {
		Long activityId = input.getScheduledActivityId();
		if (activityId == null) {
			return;
		}

		ScheduledContainerActivity activity = daoFactory.getScheduledContainerActivityDao().getById(activityId);
		if (activity == null) {
			ose.addError(ContainerActivityErrorCode.NOT_FOUND, activityId);
		}

		log.setActivity(activity);
	}

	private void setContainer(ContainerActivityLogDetail input, ContainerActivityLog log, OpenSpecimenException ose) {
		if (log.getActivity() != null) {
			log.setContainer(log.getActivity().getContainer());
			return;
		}

		StorageContainer container = null;
		Object key = null;

		if (input.getContainerId() != null) {
			container = daoFactory.getStorageContainerDao().getById(input.getContainerId());
			key = input.getContainerId();
		} else if (StringUtils.isNotBlank(input.getContainerName())) {
			container = daoFactory.getStorageContainerDao().getByName(input.getContainerName());
			key = input.getContainerName();
		}

		if (key == null) {
			ose.addError(StorageContainerErrorCode.NAME_REQUIRED);
		} else if (container == null) {
			ose.addError(StorageContainerErrorCode.NOT_FOUND, key, 1);
		} else if (container.getParentContainer() != null) {
			ose.addError(ContainerActivityErrorCode.TL_CONT_REQ, container.getName());
		}

		log.setContainer(container);
	}

	private void setTask(ContainerActivityLogDetail input, ContainerActivityLog log, OpenSpecimenException ose) {
		if (log.getActivity() != null) {
			log.setTask(log.getActivity().getTask());
			return;
		}

		ContainerTask task = null;
		Object key = null;

		if (input.getTaskId() != null) {
			task = daoFactory.getContainerTaskDao().getById(input.getTaskId());
			key = input.getTaskId();
		} else if (StringUtils.isNotBlank(input.getTaskName())) {
			task = daoFactory.getContainerTaskDao().getByName(input.getTaskName());
			key = input.getTaskName();
		}

		if (key == null) {
			ose.addError(ContainerTaskErrorCode.NAME_REQ);
		} else if (task == null) {
			ose.addError(ContainerTaskErrorCode.NOT_FOUND, key);
		}

		log.setTask(task);
	}

	private void setPerformedBy(ContainerActivityLogDetail input, ContainerActivityLog log, OpenSpecimenException ose) {
		User user = null;
		Object key = null;

		if (input.getPerformedBy() == null) {

		} else if (input.getPerformedBy().getId() != null) {
			user = daoFactory.getUserDao().getById(input.getPerformedBy().getId());
			key = input.getPerformedBy().getId();
		} else if (StringUtils.isNotBlank(input.getPerformedBy().getEmailAddress())) {
			user = daoFactory.getUserDao().getUserByEmailAddress(input.getPerformedBy().getEmailAddress());
			key = input.getPerformedBy().getEmailAddress();
		}

		if (key == null) {
			ose.addError(ContainerActivityErrorCode.PERF_BY_REQ);
		} else if (user == null) {
			ose.addError(UserErrorCode.NOT_FOUND, key);
		}

		log.setPerformedBy(user);
	}

	private void setActivityDate(ContainerActivityLogDetail input, ContainerActivityLog log, OpenSpecimenException ose) {
		if (input.getActivityDate() == null) {
			ose.addError(ContainerActivityErrorCode.DATE_REQ);
		}

		log.setActivityDate(input.getActivityDate());
	}

	private void setTimeTaken(ContainerActivityLogDetail input, ContainerActivityLog log, OpenSpecimenException ose) {
		if (input.getTimeTaken() != null && input.getTimeTaken() < 0) {
			ose.addError(ContainerActivityErrorCode.INVALID_TIME_TAKEN, input.getTimeTaken());
		}

		log.setTimeTaken(input.getTimeTaken());
	}

	private void setActivityStatus(ContainerActivityLogDetail input, ContainerActivityLog log, OpenSpecimenException ose) {
		if (StringUtils.isBlank(input.getActivityStatus())) {
			log.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
		} else if (Status.isValidActivityStatus(input.getActivityStatus())) {
			log.setActivityStatus(input.getActivityStatus());
		} else {
			ose.addError(ActivityStatusErrorCode.INVALID, input.getActivityStatus());
		}
	}
}
