package com.krishagni.catissueplus.core.query;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface ListService {
	ResponseEvent<ListConfig> getListCfg(RequestEvent<Map<String, Object>> req);

	ResponseEvent<ListDetail> getList(RequestEvent<Map<String, Object>> req);

	ResponseEvent<Integer> getListSize(RequestEvent<Map<String, Object>> req);

	ResponseEvent<Collection<Object>> getListExprValues(RequestEvent<Map<String, Object>> req);

	void registerListConfigurator(String name, Function<Map<String, Object>, ListConfig> configFn);
}
