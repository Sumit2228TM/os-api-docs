package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CpWorkflowConfig;

public class WorkflowDetail {
	private String name;

	private String view;

	private String ctrl;

	private Map<String, Object> data = new HashMap<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getCtrl() {
		return ctrl;
	}

	public void setCtrl(String ctrl) {
		this.ctrl = ctrl;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public static WorkflowDetail from(CpWorkflowConfig.Workflow workflow) {
		WorkflowDetail result = new WorkflowDetail();
		BeanUtils.copyProperties(workflow, result);
		return result;
	}

	public static Map<String, WorkflowDetail> from(Collection<CpWorkflowConfig.Workflow> workflows) {
		return workflows.stream().map(WorkflowDetail::from).collect(Collectors.toMap(WorkflowDetail::getName, w -> w));
	}
}
