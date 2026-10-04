package com.krishagni.catissueplus.core.exporter.events;

import java.util.List;
import java.util.Map;

public class ExportDetail {
	private String objectType;

	private Map<String, String> params;

	private List<Long> recordIds;

	private boolean synchronous;

	private boolean disableNotifs;

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public List<Long> getRecordIds() {
		return recordIds;
	}

	public void setRecordIds(List<Long> recordIds) {
		this.recordIds = recordIds;
	}

	public boolean isSynchronous() {
		return synchronous;
	}

	public void setSynchronous(boolean synchronous) {
		this.synchronous = synchronous;
	}

	public boolean isDisableNotifs() {
		return disableNotifs;
	}

	public void setDisableNotifs(boolean disableNotifs) {
		this.disableNotifs = disableNotifs;
	}
}
