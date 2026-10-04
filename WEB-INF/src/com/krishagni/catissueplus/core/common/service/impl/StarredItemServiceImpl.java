package com.krishagni.catissueplus.core.common.service.impl;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.domain.StarredItem;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.service.StarredItemService;
import com.krishagni.catissueplus.core.common.util.AuthUtil;

public class StarredItemServiceImpl implements StarredItemService {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public StarredItem save(String itemType, Long itemId) {
		try {
			StarredItem si = getItem(itemType, itemId);
			if (si != null) {
				return si;
			}

			si = new StarredItem();
			si.setItemType(itemType);
			si.setItemId(itemId);
			si.setUser(AuthUtil.getCurrentUser());
			daoFactory.getStarredItemDao().saveOrUpdate(si, true);
			return si;
		} catch (Exception e) {
			throw OpenSpecimenException.serverError(e);
		}
	}

	@Override
	public StarredItem delete(String itemType, Long itemId) {
		try {
			StarredItem si = getItem(itemType, itemId);
			if (si != null) {
				daoFactory.getStarredItemDao().delete(si);
			}

			return si;
		} catch (Exception e) {
			throw OpenSpecimenException.serverError(e);
		}
	}

	@Override
	public List<Long> getItemIds(String itemType, Long userId) {
		return daoFactory.getStarredItemDao().getItemIds(itemType, userId);
	}

	private StarredItem getItem(String itemType, Long itemId) {
		return daoFactory.getStarredItemDao().getItem(itemType, itemId, AuthUtil.getCurrentUser().getId());
	}
}
