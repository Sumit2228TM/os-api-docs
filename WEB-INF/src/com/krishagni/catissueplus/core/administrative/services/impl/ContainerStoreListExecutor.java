package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.ContainerStoreList;
import com.krishagni.catissueplus.core.administrative.domain.ContainerStoreList.Status;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.repository.ContainerStoreListCriteria;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;

@Configurable
public class ContainerStoreListExecutor implements ScheduledTask {
	@Autowired
	private DaoFactory daoFactory;

	@Autowired
	private StorageContainerService storageContainerSvc;

	@Override
	public void doJob(ScheduledJobRun jobRun)
	throws Exception {
		storageContainerSvc.processStoreLists(this::getPendingStoreLists);
	}

	@PlusTransactional
	private List<ContainerStoreList> getPendingStoreLists() {
		int retryInterval = ConfigUtil.getInstance().getIntSetting(ADMIN_MOD, RETRY_INTERVAL, DEF_RETRY_INTERVAL);
		int maxRetries    = ConfigUtil.getInstance().getIntSetting(ADMIN_MOD, MAX_RETRIES, DEF_RETRIES);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -retryInterval);

		ContainerStoreListCriteria crit = new ContainerStoreListCriteria()
			.statuses(Arrays.asList(Status.PENDING, Status.FAILED))
			.lastRetryTime(cal.getTime())
			.maxRetries(maxRetries)
			.maxResults(MAX_PENDING_LISTS_TO_FETCH);

		List<ContainerStoreList> storeLists = daoFactory.getContainerStoreListDao().getStoreLists(crit);
		for (ContainerStoreList list : storeLists) {
			//
			// Touch the auto freezer provider so that its data is already loaded while we are in transaction.
			//
			list.getContainer().getAutoFreezerProvider().getImplClass();
		}

		return storeLists;
	}

	private static final int MAX_PENDING_LISTS_TO_FETCH = 15;

	private static final String ADMIN_MOD = "administrative";

	private static final String MAX_RETRIES = "store_list_max_retries";

	private static final int DEF_RETRIES = 5;

	private static final String RETRY_INTERVAL = "store_list_retry_interval";

	private static final int DEF_RETRY_INTERVAL = 24;
}
