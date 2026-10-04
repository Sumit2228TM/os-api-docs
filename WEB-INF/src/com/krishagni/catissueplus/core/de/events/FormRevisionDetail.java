package com.krishagni.catissueplus.core.de.events;

import java.util.Date;

import com.krishagni.catissueplus.core.common.events.UserSummary;

public class FormRevisionDetail {
	private Long rev;

	private Integer revType;

	private UserSummary revBy;

	private Date revTime;

	private Long id;

	private String name;

	private String caption;

	private Date deletedOn;

	public Long getRev() {
		return rev;
	}

	public void setRev(Long rev) {
		this.rev = rev;
	}

	public Integer getRevType() {
		return revType;
	}

	public void setRevType(Integer revType) {
		this.revType = revType;
	}

	public UserSummary getRevBy() {
		return revBy;
	}

	public void setRevBy(UserSummary revBy) {
		this.revBy = revBy;
	}

	public Date getRevTime() {
		return revTime;
	}

	public void setRevTime(Date revTime) {
		this.revTime = revTime;
	}

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

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Date getDeletedOn() {
		return deletedOn;
	}

	public void setDeletedOn(Date deletedOn) {
		this.deletedOn = deletedOn;
	}
}
