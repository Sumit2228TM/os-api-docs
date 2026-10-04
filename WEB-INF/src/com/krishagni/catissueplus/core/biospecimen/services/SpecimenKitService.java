package com.krishagni.catissueplus.core.biospecimen.services;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenKit;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenKitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenKitSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenKitListCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.events.QueryDataExportResult;

public interface SpecimenKitService {

	ResponseEvent<List<SpecimenKitSummary>> getSpecimenKits(RequestEvent<SpecimenKitListCriteria> req);

	ResponseEvent<SpecimenKitDetail> getSpecimenKit(RequestEvent<Long> req);

	ResponseEvent<SpecimenKitDetail> createSpecimenKit(RequestEvent<SpecimenKitDetail> req);

	ResponseEvent<SpecimenKitDetail> updateSpecimenKit(RequestEvent<SpecimenKitDetail> req);

	ResponseEvent<QueryDataExportResult> exportReport(RequestEvent<Long> req);

	//
	// internal APIs
	//
	SpecimenKit createSpecimenKit(SpecimenKitDetail kitDetail, List<Specimen> specimens);
}
