package com.krishagni.catissueplus.core.common.service;

import java.util.List;

import com.krishagni.catissueplus.core.common.domain.StarredItem;

//
// for now, this is very simple save service.
// in future it will do authorisation of item tags.
//
public interface StarredItemService {
	StarredItem save(String itemType, Long itemId);

	StarredItem delete(String itemType, Long itemId);

	List<Long> getItemIds(String itemType, Long userId);
}
