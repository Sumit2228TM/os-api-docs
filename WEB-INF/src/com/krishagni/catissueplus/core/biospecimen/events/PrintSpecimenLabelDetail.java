package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.List;

public class PrintSpecimenLabelDetail {
	private List<Long> specimenIds = new ArrayList<>();
	
	private List<String> specimenLabels = new ArrayList<>();
	
	private Long visitId;
	
	private String visitName;

	private Long cprId;

	private String cpShortTitle;

	private String ppid;
	
	private int numCopies = 1;

	public List<Long> getSpecimenIds() {
		return specimenIds;
	}

	public void setSpecimenIds(List<Long> specimenIds) {
		this.specimenIds = specimenIds;
	}

	public List<String> getSpecimenLabels() {
		return specimenLabels;
	}

	public void setSpecimenLabels(List<String> specimenLabels) {
		this.specimenLabels = specimenLabels;
	}

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public String getVisitName() {
		return visitName;
	}

	public void setVisitName(String visitName) {
		this.visitName = visitName;
	}

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

	public String getCpShortTitle() {
		return cpShortTitle;
	}

	public void setCpShortTitle(String cpShortTitle) {
		this.cpShortTitle = cpShortTitle;
	}

	public String getPpid() {
		return ppid;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}

	public int getNumCopies() {
		return numCopies;
	}

	public void setNumCopies(int numCopies) {
		this.numCopies = numCopies;
	} 
}
