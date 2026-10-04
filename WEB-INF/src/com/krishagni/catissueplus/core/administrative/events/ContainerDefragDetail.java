package com.krishagni.catissueplus.core.administrative.events;

public class ContainerDefragDetail {
	private Long id;

	private String name;

	private String fileId;

	private int movedSpecimensCount;

	private boolean aliquotsInSameContainer;

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

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public int getMovedSpecimensCount() {
		return movedSpecimensCount;
	}

	public void setMovedSpecimensCount(int movedSpecimensCount) {
		this.movedSpecimensCount = movedSpecimensCount;
	}

	public boolean isAliquotsInSameContainer() {
		return aliquotsInSameContainer;
	}

	public void setAliquotsInSameContainer(boolean aliquotsInSameContainer) {
		this.aliquotsInSameContainer = aliquotsInSameContainer;
	}
}
