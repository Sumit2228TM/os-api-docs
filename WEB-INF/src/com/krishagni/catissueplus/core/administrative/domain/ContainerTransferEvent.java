package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class ContainerTransferEvent extends BaseEntity {
	private StorageContainer container;

	private Site fromSite;

	private StorageContainer fromContainer;

	private String fromRow;

	private String fromColumn;

	private Integer fromRowOrdinal;

	private Integer fromColumnOrdinal;

	private Integer fromPosition;

	private Site toSite;

	private StorageContainer toContainer;

	private String toRow;

	private String toColumn;

	private Integer toRowOrdinal;

	private Integer toColumnOrdinal;

	private Integer toPosition;

	private User user;

	private Date time;

	private String reason;

	public StorageContainer getContainer() {
		return container;
	}

	public void setContainer(StorageContainer container) {
		this.container = container;
	}

	public Site getFromSite() {
		return fromSite;
	}

	public void setFromSite(Site fromSite) {
		this.fromSite = fromSite;
	}

	public StorageContainer getFromContainer() {
		return fromContainer;
	}

	public void setFromContainer(StorageContainer fromContainer) {
		this.fromContainer = fromContainer;
	}

	public String getFromRow() {
		return fromRow;
	}

	public void setFromRow(String fromRow) {
		this.fromRow = fromRow;
	}

	public String getFromColumn() {
		return fromColumn;
	}

	public void setFromColumn(String fromColumn) {
		this.fromColumn = fromColumn;
	}

	public Integer getFromRowOrdinal() {
		return fromRowOrdinal;
	}

	public void setFromRowOrdinal(Integer fromRowOrdinal) {
		this.fromRowOrdinal = fromRowOrdinal;
	}

	public Integer getFromColumnOrdinal() {
		return fromColumnOrdinal;
	}

	public void setFromColumnOrdinal(Integer fromColumnOrdinal) {
		this.fromColumnOrdinal = fromColumnOrdinal;
	}

	public Integer getFromPosition() {
		return fromPosition;
	}

	public void setFromPosition(Integer fromPosition) {
		this.fromPosition = fromPosition;
	}

	public ContainerTransferEvent fromLocation(Site site, StorageContainer container, StorageContainerPosition position) {
		setFromSite(site);
		setFromContainer(container);
		if (position == null) {
			return this;
		}

		setFromRow(position.getPosTwo());
		setFromColumn(position.getPosOne());

		setFromRowOrdinal(position.getPosTwoOrdinal());
		setFromColumnOrdinal(position.getPosOneOrdinal());

		setFromPosition(position.getPosition());
		return this;
	}

	public Site getToSite() {
		return toSite;
	}

	public void setToSite(Site toSite) {
		this.toSite = toSite;
	}

	public StorageContainer getToContainer() {
		return toContainer;
	}

	public void setToContainer(StorageContainer toContainer) {
		this.toContainer = toContainer;
	}

	public String getToRow() {
		return toRow;
	}

	public void setToRow(String toRow) {
		this.toRow = toRow;
	}

	public String getToColumn() {
		return toColumn;
	}

	public void setToColumn(String toColumn) {
		this.toColumn = toColumn;
	}

	public Integer getToRowOrdinal() {
		return toRowOrdinal;
	}

	public void setToRowOrdinal(Integer toRowOrdinal) {
		this.toRowOrdinal = toRowOrdinal;
	}

	public Integer getToColumnOrdinal() {
		return toColumnOrdinal;
	}

	public void setToColumnOrdinal(Integer toColumnOrdinal) {
		this.toColumnOrdinal = toColumnOrdinal;
	}

	public Integer getToPosition() {
		return toPosition;
	}

	public void setToPosition(Integer toPosition) {
		this.toPosition = toPosition;
	}

	public ContainerTransferEvent toLocation(Site site, StorageContainer container, StorageContainerPosition position) {
		setToSite(site);
		setToContainer(container);
		if (position == null) {
			return this;
		}

		setToRow(position.getPosTwo());
		setToColumn(position.getPosOne());

		setToRowOrdinal(position.getPosTwoOrdinal());
		setToColumnOrdinal(position.getPosOneOrdinal());

		setToPosition(position.getPosition());
		return this;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
