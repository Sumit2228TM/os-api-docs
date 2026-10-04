
package com.krishagni.catissueplus.core.biospecimen.domain;

public class StagedParticipantMedicalIdentifier extends BaseEntity {
	private String site;

	private String medicalRecordNumber;

	private StagedParticipant participant;

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getMedicalRecordNumber() {
		return medicalRecordNumber;
	}

	public void setMedicalRecordNumber(String medicalRecordNumber) {
		this.medicalRecordNumber = medicalRecordNumber;
	}

	public StagedParticipant getParticipant() {
		return participant;
	}

	public void setParticipant(StagedParticipant participant) {
		this.participant = participant;
	}
}