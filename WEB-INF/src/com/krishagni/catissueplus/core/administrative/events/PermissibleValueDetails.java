
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.common.util.Utility;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissibleValueDetails {

	private Long id;

	private Long parentId;
	
	private String parentValue;

	private String value;

	private String attribute;

	private String conceptCode;

	private Map<String, String> props = new HashMap<>();

	private String activityStatus = "Active";
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentValue() {
		return parentValue;
	}

	public void setParentValue(String parentValue) {
		this.parentValue = parentValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getConceptCode() {
		return conceptCode;
	}

	public void setConceptCode(String conceptCode) {
		this.conceptCode = conceptCode;
	}

	public Map<String, String> getProps() {
		return props;
	}

	public void setProps(Map<String, String> props) {
		this.props = props;
	}

	//
	// For BO Template
	//
	public void setPropMap(List<Map<String, String>> propMap) {
		this.props = propMap.stream().collect(Collectors.toMap(p -> p.get("name"), p -> p.get("value")));
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	public List<Map<String, String>> getPropMap() {
		if (props == null || props.isEmpty()) {
			return Collections.emptyList();
		}

		List<Map<String, String>> result = new ArrayList<>();
		for (Map.Entry<String, String> pe : props.entrySet()) {
			Map<String, String> r = new HashMap<>();
			r.put("name",  pe.getKey());
			r.put("value", pe.getValue());
			result.add(r);
		}

		return result;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public static PermissibleValueDetails fromDomain(PermissibleValue pv) {
		PermissibleValueDetails result = new PermissibleValueDetails();
		result.setConceptCode(pv.getConceptCode());
		result.setId(pv.getId());
		result.setAttribute(pv.getAttribute());
		result.setValue(pv.getValue());
		result.setActivityStatus(pv.getActivityStatus());
		if (pv.getParent() != null) {
			result.setParentId(pv.getParent().getId());
			result.setParentValue(pv.getParent().getValue());
		}
		
		if (pv.getProps() != null && !pv.getProps().isEmpty()) {
			result.setProps(pv.getProps());
		}
		
		return result;
	}

	public static PermissibleValueDetails from(PermissibleValue pv) {
		return fromDomain(pv);
	}

	public static List<PermissibleValueDetails> from(Collection<PermissibleValue> pvs) {
		return Utility.nullSafeStream(pvs).map(PermissibleValueDetails::fromDomain).collect(Collectors.toList());
	}
}
