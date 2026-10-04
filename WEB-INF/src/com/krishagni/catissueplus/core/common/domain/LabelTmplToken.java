package com.krishagni.catissueplus.core.common.domain;

public interface LabelTmplToken {
	String EMPTY_VALUE = "##!EMPTY_VALUE!##";

	String getName();

	boolean areArgsValid(String ... args);

	String getReplacement(Object object);

	String getReplacement(Object object, String ... args);

	int validate(Object object, String input, int startIdx, String ... args);
}

