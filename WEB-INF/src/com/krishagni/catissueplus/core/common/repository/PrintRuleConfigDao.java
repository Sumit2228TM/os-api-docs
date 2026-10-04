package com.krishagni.catissueplus.core.common.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.domain.PrintRuleConfig;

public interface PrintRuleConfigDao extends Dao<PrintRuleConfig> {
	List<PrintRuleConfig> getPrintRules(PrintRuleConfigsListCriteria crit);
}
