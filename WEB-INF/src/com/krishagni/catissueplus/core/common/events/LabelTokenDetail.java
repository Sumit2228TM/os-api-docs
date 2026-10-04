package com.krishagni.catissueplus.core.common.events;

import java.util.List;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;
import com.krishagni.catissueplus.core.common.util.MessageUtil;

public class LabelTokenDetail {
	private String name;

	private String displayName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public static LabelTokenDetail from(String msgPrefix, LabelTmplToken token) {
		LabelTokenDetail detail = new LabelTokenDetail();
		detail.setName(token.getName());
		detail.setDisplayName(MessageUtil.getInstance().getMessage(msgPrefix + token.getName(), null));
		return detail;
	}

	public static List<LabelTokenDetail> from(String msgPrefix, List<LabelTmplToken> tokens) {
		return tokens.stream().map(t -> from(msgPrefix, t)).collect(Collectors.toList());
	}
}
