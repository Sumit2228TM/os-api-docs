package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenKit;

public class SpecimenKitDetail extends SpecimenKitSummary {

	private List<SpecimenInfo> specimens;

	private String comments;

	private String activityStatus;

	public List<SpecimenInfo> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(List<SpecimenInfo> specimens) {
		this.specimens = specimens;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public static SpecimenKitDetail from(SpecimenKit kit) {
		SpecimenKitDetail detail = new SpecimenKitDetail();
		SpecimenKitSummary.copy(kit, detail);
		detail.setSpecimens(SpecimenInfo.from(new ArrayList<>(kit.getSpecimens())));
		detail.setComments(kit.getComments());
		detail.setActivityStatus(kit.getActivityStatus());
		return detail;
	}
}
