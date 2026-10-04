package com.krishagni.catissueplus.core.biospecimen.events;

public class VisitSearchDetail {
	public enum SearchAttr { EMPI_MRN, VISIT_NAME, SPR_NO}

	private Long cpId;

	private SearchAttr attr;

	private String value;

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public SearchAttr getAttr() {
		return attr;
	}

	public void setAttr(SearchAttr attr) {
		this.attr = attr;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
