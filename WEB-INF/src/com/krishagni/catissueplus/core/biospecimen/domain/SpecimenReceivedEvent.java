package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Date;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.domain.User;

//
// Added purely for backward compatibility w.r.t labels generation and printing
//
public class SpecimenReceivedEvent {
	private Specimen specimen;

	public SpecimenReceivedEvent(Specimen specimen) {
		this.specimen = specimen;
	}

	public PermissibleValue getQuality() {
		return specimen.getReceivedQuality();
	}

	public User getUser() {
		return specimen.getReceivedUser();
	}

	public Date getTime() {
		return specimen.getReceivedTime();
	}

	public String getComments() {
		return specimen.getReceivedComments();
	}
}
