
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;

public interface VisitFactory {
	Visit createVisit(VisitDetail visitDetail);
	
	Visit createVisit(Visit existing, VisitDetail visitDetail);
}
