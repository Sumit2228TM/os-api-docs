package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AutoFreezerProviderDetail {
	private Long id;

	private String name;

	private String implClass;

	private Map<String, String> props = new HashMap<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImplClass() {
		return implClass;
	}

	public void setImplClass(String implClass) {
		this.implClass = implClass;
	}

	public Map<String, String> getProps() {
		return props;
	}

	public void setProps(Map<String, String> props) {
		this.props = props;
	}

	public static AutoFreezerProviderDetail from(AutoFreezerProvider provider) {
		AutoFreezerProviderDetail detail = new AutoFreezerProviderDetail();
		detail.setId(provider.getId());
		detail.setName(provider.getName());
		detail.setImplClass(provider.getImplClass());
		detail.setProps(provider.getProps());
		return detail;
	}

	public static List<AutoFreezerProviderDetail> from(Collection<AutoFreezerProvider> providers) {
		return providers.stream().map(AutoFreezerProviderDetail::from).collect(Collectors.toList());
	}
}
