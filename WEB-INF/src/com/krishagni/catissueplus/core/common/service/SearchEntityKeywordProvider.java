package com.krishagni.catissueplus.core.common.service;

import java.util.List;

import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;

import com.krishagni.catissueplus.core.common.domain.SearchEntityKeyword;

public interface SearchEntityKeywordProvider {
	String getEntity();

	List<SearchEntityKeyword> getKeywords(PostInsertEvent event);

	List<SearchEntityKeyword> getKeywords(PostUpdateEvent event);

	List<SearchEntityKeyword> getKeywords(PostDeleteEvent event);
}
