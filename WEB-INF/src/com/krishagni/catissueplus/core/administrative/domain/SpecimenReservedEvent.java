package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class SpecimenReservedEvent extends BaseEntity {
	private Specimen specimen;

	private DistributionProtocol dp;

	private SpecimenReservedEvent cancelledEvent;

	private User user;

	private Date time;

	private String comments;

	public Specimen getSpecimen() {
		return specimen;
	}

	public void setSpecimen(Specimen specimen) {
		this.specimen = specimen;
	}

	public DistributionProtocol getDp() {
		return dp;
	}

	public void setDp(DistributionProtocol dp) {
		this.dp = dp;
	}

	public SpecimenReservedEvent getCancelledEvent() {
		return cancelledEvent;
	}

	public void setCancelledEvent(SpecimenReservedEvent cancelledEvent) {
		this.cancelledEvent = cancelledEvent;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
