package com.krishagni.catissueplus.core.biospecimen.print;

import java.util.Collections;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.util.ExpressionUtil;

public class EvalExprPrintToken extends AbstractLabelTmplToken {
	private String objectName;

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	@Override
	public String getName() {
		return "eval";
	}

	@Override
	public String getReplacement(Object object) {
		throw new IllegalArgumentException("Invalid number of input parameters. Require input expression to evaluate");
	}

	@Override
	public String getReplacement(Object object, String... args) {
		if (args == null || args.length < 1) {
			throw new IllegalArgumentException("Invalid number of input parameters. Require input expression to evaluate");
		}

		String expr = String.join(",", args);
		Object result = ExpressionUtil.getInstance().evaluate(expr, Collections.singletonMap(getObjectName(), object));
		return result != null ? result.toString() : StringUtils.EMPTY;
	}
}