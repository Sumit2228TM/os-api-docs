package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum SpecimenKitErrorCode implements ErrorCode {
    NOT_FOUND,

    SPECIMENS_REQUIRED,

    INVALID_SPECIMENS,

    SEND_SITE_REQUIRED,

    INVALID_SEND_SITE,

    RECV_SITE_REQUIRED,

    INVALID_RECV_SITE,

    RPT_TMPL_NOT_CONF;

    @Override
    public String code() {
        return "SPECIMEN_KIT_" + this.name();
    }
}
