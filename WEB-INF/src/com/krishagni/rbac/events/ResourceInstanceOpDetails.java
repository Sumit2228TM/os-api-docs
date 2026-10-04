package com.krishagni.rbac.events;

import com.krishagni.rbac.domain.ResourceInstanceOp;

public class ResourceInstanceOpDetails {
	private String operationName;

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	public static ResourceInstanceOpDetails fromResourceInstanceOp(ResourceInstanceOp op) {
		ResourceInstanceOpDetails rd = new ResourceInstanceOpDetails();
		rd.setOperationName(op.getOperation().getName());
		return rd;
	}
}
