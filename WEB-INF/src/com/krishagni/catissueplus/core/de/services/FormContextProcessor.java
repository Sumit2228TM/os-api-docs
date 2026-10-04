package com.krishagni.catissueplus.core.de.services;

import krishagni.catissueplus.beans.FormContextBean;

public interface FormContextProcessor {
	void onSaveOrUpdate(FormContextBean formCtxt);

	void onRemove(FormContextBean formCtxt);
}