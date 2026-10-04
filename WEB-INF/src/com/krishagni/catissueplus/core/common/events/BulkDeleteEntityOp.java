package com.krishagni.catissueplus.core.common.events;

import java.util.Set;

public class BulkDeleteEntityOp {
	private Set<Long> ids;

	private boolean close;

	private boolean forceDelete;

	private String reason;

	public BulkDeleteEntityOp() {}

	public BulkDeleteEntityOp(Set<Long> ids) {
		this.ids = ids;
	}

	public Set<Long> getIds() {
		return ids;
	}

	public void setIds(Set<Long> ids) {
		this.ids = ids;
	}

	public boolean isClose() {
		return close;
	}

	public void setClose(boolean close) {
		this.close = close;
	}

	public boolean isForceDelete() {
		return forceDelete;
	}

	public void setForceDelete(boolean forceDelete) {
		this.forceDelete = forceDelete;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
