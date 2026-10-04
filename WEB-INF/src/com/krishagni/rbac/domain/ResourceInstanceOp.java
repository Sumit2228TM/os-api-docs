package com.krishagni.rbac.domain;

import org.hibernate.envers.Audited;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

@Audited
public class ResourceInstanceOp extends BaseEntity {
	private RoleAccessControl accessControl;

	private Operation operation;

	public RoleAccessControl getAccessControl() {
		return accessControl;
	}

	public void setAccessControl(RoleAccessControl accessControl) {
		this.accessControl = accessControl;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}	
}
