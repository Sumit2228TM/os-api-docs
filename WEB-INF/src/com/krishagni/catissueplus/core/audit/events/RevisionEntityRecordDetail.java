package com.krishagni.catissueplus.core.audit.events;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.audit.domain.RevisionEntityRecord;

public class RevisionEntityRecordDetail {
	private Long id;

	private int type;

	private String entityName;

	private Long entityId;

	private String modifiedProps;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getModifiedProps() {
		return modifiedProps;
	}

	public void setModifiedProps(String modifiedProps) {
		this.modifiedProps = modifiedProps;
	}

	public String toString() {
		return new StringBuilder("[")
			.append("id = ").append(getId()).append(", ")
			.append("type = ").append(getType()).append(", ")
			.append("entity = ").append(getEntityName()).append(", ")
			.append("entity id = ").append(getEntityId()).append(", ")
			.append("modified props = ").append(getModifiedProps())
			.append("]")
			.toString();
	}

	public static RevisionEntityRecordDetail from(RevisionEntityRecord record) {
		RevisionEntityRecordDetail result = new RevisionEntityRecordDetail();
		result.setId(record.getId());
		result.setType(record.getType());
		result.setEntityName(record.getEntityName());
		result.setEntityId(record.getEntityId());
		return result;
	}

	public static List<RevisionEntityRecordDetail> from(Collection<RevisionEntityRecord> recs) {
		return recs.stream().map(RevisionEntityRecordDetail::from).collect(Collectors.toList());
	}
}
