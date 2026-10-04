package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.User;

public class SpecimenChildrenEvent extends BaseEntity {
	private String lineage;

	private Specimen specimen;

	private User user;

	private Date time;

	private String comments;

	private Set<Specimen> children = new LinkedHashSet<>();

	public SpecimenChildrenEvent() {

	}

	public String getLineage() {
		return lineage;
	}

	public void setLineage(String lineage) {
		this.lineage = lineage;
	}

	public Specimen getSpecimen() {
		return specimen;
	}

	public void setSpecimen(Specimen specimen) {
		this.specimen = specimen;
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

	public Set<Specimen> getChildren() {
		return children;
	}

	public void setChildren(Set<Specimen> children) {
		this.children = children;
	}

	public void addChild(Specimen childSpmn) {
		childSpmn.setParentEvent(this);
		children.add(childSpmn);
	}

	public void removeChild(Specimen childSpmn) {
		childSpmn.setParentEvent(null);
		children.remove(childSpmn);
	}
}