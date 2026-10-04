package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenKit;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenKitDetail;

public interface SpecimenKitFactory {
    SpecimenKit createSpecimenKit(SpecimenKitDetail detail);

    SpecimenKit createSpecimenKit(SpecimenKitDetail detail, List<Specimen> specimens);

}
