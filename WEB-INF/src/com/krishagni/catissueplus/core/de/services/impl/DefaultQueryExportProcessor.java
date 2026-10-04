package com.krishagni.catissueplus.core.de.services.impl;

import java.io.OutputStream;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolGroup;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.MessageUtil;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.catissueplus.core.de.services.QueryService;

@Configurable
public class DefaultQueryExportProcessor implements QueryService.ExportProcessor {
	private static final String QUERY_EXPORTED_BY = "query_exported_by";

	private static final String QUERY_EXPORTED_ON = "query_exported_on";

	private static final String QUERY_CP = "query_cp";

	private static final String QUERY_CP_GROUP = "query_cp_group";

	@Autowired
	private DaoFactory daoFactory;

	private Long cpId;

	private Long cpGroupId;

	public DefaultQueryExportProcessor(Long cpId, Long cpGroupId) {
		this.cpId = cpId;
		this.cpGroupId = cpGroupId;
	}

	@Override
	public String filename() {
		return null;
	}

	@Override
	public void headers(OutputStream out) {
		Map<String, String> headers = new LinkedHashMap<>();
		headers.put(msg(QUERY_EXPORTED_BY), AuthUtil.getCurrentUser().formattedName());
		headers.put(msg(QUERY_EXPORTED_ON), Utility.getDateTimeString(Calendar.getInstance().getTime()));

		if (cpGroupId != null && cpGroupId != -1L) {
			CollectionProtocolGroup group = daoFactory.getCpGroupDao().getById(cpGroupId);
			if (group != null) {
				headers.put(msg(QUERY_CP_GROUP), group.getName());
			}
		} else if (cpId != null && cpId != -1L) {
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getById(cpId);
			if (cp != null) {
				headers.put(msg(QUERY_CP), cp.getShortTitle());
			}
		}

		headers.put("", "");
		Utility.writeKeyValuesToCsv(out, headers);
	}

	private String msg(String key) {
		return MessageUtil.getInstance().getMessage(key);
	}
}