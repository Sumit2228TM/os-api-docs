package com.krishagni.catissueplus.core.common.events;

import java.util.HashMap;
import java.util.Map;

public class DeleteEntityOp {
	
	private Long id;
	
	private boolean close;
	
	private boolean forceDelete;

	private Map<String, Object> params = new HashMap<>();
	
	public DeleteEntityOp() {}
	
	public DeleteEntityOp(Long id, boolean close) {
		this.id = id;
		this.close = close;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Object getParam(String name) {
		return params.get(name);
	}

	public void setParam(String name, Object value) {
		params.put(name, value);
	}

	public boolean booleanParam(String name) {
		return Boolean.TRUE.equals(getParam(name));
	}

	public Integer intParam(String param) {
		Object value = getParam(param);
		if (value == null) {
			return null;
		} else if (value instanceof Number) {
			return ((Number) value).intValue();
		} else if (value instanceof String) {
			return Integer.parseInt((String) value);
		} else {
			throw new IllegalArgumentException(param + " value is not an integer");
		}
	}

	public String stringParam(String param) {
		Object value = getParam(param);
		return value == null ? null : value.toString();
	}
}
