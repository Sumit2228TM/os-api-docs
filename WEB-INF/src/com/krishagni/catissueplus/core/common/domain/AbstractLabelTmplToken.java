package com.krishagni.catissueplus.core.common.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractLabelTmplToken implements LabelTmplToken {

	@Override
	public boolean areArgsValid(String ... args) {
		return true;
	}

	@Override
	public String getReplacement(Object object, String ... args) {
		return getReplacement(object);
	}

	@Override
	public int validate(Object object, String input, int startIdx, String ... args) {
		String label = getReplacement(object, args);
		
		int endIdx = startIdx + label.length();
		if (startIdx >= input.length() || endIdx > input.length()) {
			return startIdx;
		}
		
		if (input.substring(startIdx, endIdx).equals(label)) {
			return endIdx;
		}
		
		return startIdx;
	}
	
	protected int validateNumber(String input, int startIdx, String ... args) {
		String regx = "\\d+";
		if (args != null && args.length > 0) {
			int digit = Integer.parseInt(args[0]);
			regx = digit == 0 ? regx : "\\d{" + digit + "}";
		}

		Pattern pattern = Pattern.compile(regx);
		Matcher matcher = pattern.matcher(input.substring(startIdx));
		if (matcher.find() && matcher.start() == 0) {
			startIdx += matcher.group(0).length();
		}

		return startIdx;
	}

	protected String formatNumber(Number number, String... args) {
		String format = "%d";
		if (args != null && args.length > 0) {
			int digit = Integer.parseInt(args[0]);
			format = digit == 0 ? format : "%0" + digit + "d";
		}

		return String.format(format, number);
	}
}
