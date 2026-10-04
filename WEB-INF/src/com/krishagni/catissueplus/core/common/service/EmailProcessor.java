package com.krishagni.catissueplus.core.common.service;

import com.krishagni.catissueplus.core.common.domain.Email;

public interface EmailProcessor {
	String getName();

	void process(Email email);
}
