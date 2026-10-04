package com.krishagni.catissueplus.core.de.events;

public class MoveFormRecordsOp {
	private String formName;

	private String entity;

	private String sourceCp;

	private String sourceCpGroup;

	private String targetCp;

	private String targetCpGroup;

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getSourceCp() {
		return sourceCp;
	}

	public void setSourceCp(String sourceCp) {
		this.sourceCp = sourceCp;
	}

	public String getSourceCpGroup() {
		return sourceCpGroup;
	}

	public void setSourceCpGroup(String sourceCpGroup) {
		this.sourceCpGroup = sourceCpGroup;
	}

	public String getTargetCp() {
		return targetCp;
	}

	public void setTargetCp(String targetCp) {
		this.targetCp = targetCp;
	}

	public String getTargetCpGroup() {
		return targetCpGroup;
	}

	public void setTargetCpGroup(String targetCpGroup) {
		this.targetCpGroup = targetCpGroup;
	}
}
