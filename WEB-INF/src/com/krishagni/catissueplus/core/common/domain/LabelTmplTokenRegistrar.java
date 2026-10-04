package com.krishagni.catissueplus.core.common.domain;

import java.util.List;

public interface LabelTmplTokenRegistrar {
	List<LabelTmplToken> getTokens();

	void register(LabelTmplToken token);
	
	LabelTmplToken getToken(String tokenName);
}
