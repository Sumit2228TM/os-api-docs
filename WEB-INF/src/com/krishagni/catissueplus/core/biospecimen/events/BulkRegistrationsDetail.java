package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

public class BulkRegistrationsDetail {
    private Long cpId;

    private String cpTitle;

    private String cpShortTitle;

    private int regCount;

    private String collectionSite;

    private List<CollectionProtocolEventDetail> events;

    //
    // optionally specify details of kit to be created
    //
    private SpecimenKitDetail kitDetail;

    public Long getCpId() {
        return cpId;
    }

    public void setCpId(Long cpId) {
        this.cpId = cpId;
    }

    public String getCpTitle() {
        return cpTitle;
    }

    public void setCpTitle(String cpTitle) {
        this.cpTitle = cpTitle;
    }

    public String getCpShortTitle() {
        return cpShortTitle;
    }

    public void setCpShortTitle(String cpShortTitle) {
        this.cpShortTitle = cpShortTitle;
    }

    public Integer getRegCount() {
        return regCount;
    }

    public void setRegCount(int regCount) {
        this.regCount = regCount;
    }

    public String getCollectionSite() {
        return collectionSite;
    }

    public void setCollectionSite(String collectionSite) {
        this.collectionSite = collectionSite;
    }

    public List<CollectionProtocolEventDetail> getEvents() {
        return events;
    }

    public void setEvents(List<CollectionProtocolEventDetail> events) {
        this.events = events;
    }

    public SpecimenKitDetail getKitDetail() {
        return kitDetail;
    }

    public void setKitDetail(SpecimenKitDetail kitDetail) {
        this.kitDetail = kitDetail;
    }
}
