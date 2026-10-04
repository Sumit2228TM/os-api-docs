package com.krishagni.catissueplus.core.common.domain.factory;

import com.krishagni.catissueplus.core.common.domain.PrintRuleConfig;
import com.krishagni.catissueplus.core.common.events.PrintRuleConfigDetail;

public interface PrintRuleConfigFactory {
	PrintRuleConfig createPrintRuleConfig(PrintRuleConfigDetail detail);
}