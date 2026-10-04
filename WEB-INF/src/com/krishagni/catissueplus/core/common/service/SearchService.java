package com.krishagni.catissueplus.core.common.service;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.SearchResult;

public interface SearchService {
	List<SearchResult> search(String keyword, int maxResults);

	void registerKeywordProvider(SearchEntityKeywordProvider provider);

	void registerSearchResultProcessor(SearchResultProcessor processor);
}
