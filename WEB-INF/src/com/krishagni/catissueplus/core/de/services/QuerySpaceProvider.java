package com.krishagni.catissueplus.core.de.services;

import edu.common.dynamicextensions.query.QuerySpace;

public interface QuerySpaceProvider {
	QuerySpace getQuerySpace(String name);
}
