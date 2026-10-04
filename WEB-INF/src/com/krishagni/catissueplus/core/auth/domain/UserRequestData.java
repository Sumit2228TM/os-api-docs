package com.krishagni.catissueplus.core.auth.domain;

import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.User;

public class UserRequestData {
	private static final UserRequestData INSTANCE = new UserRequestData();

	private ThreadLocal<Map<String, Object>> data = new ThreadLocal<Map<String, Object>>() {
		@Override
		protected Map<String, Object> initialValue() {
			return new HashMap<>();
		}
	};

	private UserRequestData() {

	}

	public static UserRequestData getInstance() {
		return INSTANCE;
	}

	public void setData(Map<String, Object> input) {
		data.get().putAll(input);
	}

	public Map<String, Object> getData() {
		return data.get();
	}

	public Object getDataItem(String name) {
		return data.get().get(name);
	}

	public void cleanup() {
		data.remove();
	}

	public User getUser() {
		return (User) data.get().get("$user");
	}

}
