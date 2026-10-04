package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;

public class PrintContainerLabelDetail {
	private List<Long> containerIds;

	private List<String> containerNames;

	private int copies = 1;

	public List<Long> getContainerIds() {
		return containerIds;
	}

	public void setContainerIds(List<Long> containerIds) {
		this.containerIds = containerIds;
	}

	public List<String> getContainerNames() {
		return containerNames;
	}

	public void setContainerNames(List<String> containerNames) {
		this.containerNames = containerNames;
	}

	public int getCopies() {
		return copies;
	}

	public void setCopies(int copies) {
		this.copies = copies;
	}
}
