package com.krishagni.catissueplus.core.common.domain;

import java.util.Map;

import com.krishagni.catissueplus.core.common.domain.factory.LabelPrintRuleFactory;

public class LabelPrintRuleFactoryRegistrar {
	private Map<String, LabelPrintRuleFactory> printRuleFactories;

	private static LabelPrintRuleFactoryRegistrar instance;

	public void setPrintRuleFactories(Map<String, LabelPrintRuleFactory> printRuleFactories) {
		this.printRuleFactories = printRuleFactories;
	}

	public LabelPrintRuleFactory getFactory(String objectType) {
		return printRuleFactories.get(objectType);
	}

	public static LabelPrintRuleFactoryRegistrar getInstance() {
		if (instance == null || instance.printRuleFactories == null) {
			instance = new LabelPrintRuleFactoryRegistrar();
		}

		return instance;
	}
}