package com.krishagni.catissueplus.core.common.events;

public class NameValuePair {
	private String name;

	private String value;

	public NameValuePair() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static NameValuePair create(String name, String value) {
		NameValuePair pair = new NameValuePair();
		pair.setName(name);
		pair.setValue(value);
		return pair;
	}
}
