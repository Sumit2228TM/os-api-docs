package com.krishagni.catissueplus.core.biospecimen.matching;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.MatchedVisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSearchDetail;

public interface VisitsLookup {
	List<MatchedVisitDetail> getVisits(VisitSearchDetail input);
}
