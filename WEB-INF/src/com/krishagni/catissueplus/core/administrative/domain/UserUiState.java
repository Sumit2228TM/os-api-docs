package com.krishagni.catissueplus.core.administrative.domain;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UserUiState {
	private Long userId;

	private Map<String, Object> state;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Map<String, Object> getState() {
		return state;
	}

	public void setState(Map<String, Object> state) {
		this.state = state;
	}

	public String getStateJson() {
		try {
			if (state == null || state.isEmpty()) {
				return null;
			}

			return new ObjectMapper().writeValueAsString(state);
		} catch (Exception e) {
			throw new RuntimeException("Error transforming the state object to JSON string", e);
		}
	}

	public void setStateJson(String json) {
		try {
			if (StringUtils.isBlank(json)) {
				this.state = new HashMap<>();
				return;
			}

			this.state = new ObjectMapper().readValue(json, Map.class);
		} catch (Exception e) {
			throw new RuntimeException("Error transforming JSON string to state object", e);
		}
	}
}
