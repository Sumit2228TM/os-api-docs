package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.CollectionUpdater;

@Audited
public class SpecimenKit extends BaseEntity {
    private static final String ENTITY_NAME = "specimen_kit";

    public CollectionProtocol collectionProtocol;

    public Site sendingSite;

    public Site receivingSite;

    public Date sendingDate;

    public User sender;

    private Set<Specimen> specimens = new HashSet<>();

    public String comments;

    public String activityStatus;

    public static String getEntityName() {
        return ENTITY_NAME;
    }

    public CollectionProtocol getCollectionProtocol() {
        return collectionProtocol;
    }

    public void setCollectionProtocol(CollectionProtocol collectionProtocol) {
        this.collectionProtocol = collectionProtocol;
    }

    public Site getSendingSite() {
        return sendingSite;
    }

    public void setSendingSite(Site sendingSite) {
        this.sendingSite = sendingSite;
    }

    public Site getReceivingSite() {
        return receivingSite;
    }

    public void setReceivingSite(Site receivingSite) {
        this.receivingSite = receivingSite;
    }

    public Date getSendingDate() {
        return sendingDate;
    }

    public void setSendingDate(Date sendingDate) {
        this.sendingDate = sendingDate;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    @NotAudited
    public Set<Specimen> getSpecimens() {
        return specimens;
    }

    public void setSpecimens(Set<Specimen> specimens) {
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

    public void update(SpecimenKit kit) {
        setCollectionProtocol(kit.getCollectionProtocol());
        setSendingSite(kit.getSendingSite());
        setReceivingSite(kit.getReceivingSite());
        setSendingDate(kit.getSendingDate());
        setSender(kit.getSender());
        setComments(kit.getComments());
        setActivityStatus(kit.getActivityStatus());

        CollectionUpdater.update(getSpecimens(), kit.getSpecimens());
    }
}
