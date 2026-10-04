package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.services.impl.CpWorkflowTxnCache;
import com.krishagni.catissueplus.core.common.util.ExpressionUtil;
import com.krishagni.catissueplus.core.de.events.ExtensionDetail;

public class LabelSettingsUtil {
	public static String getLabelFormat(Specimen specimen) {
		Map<String, Object> labelSettings = getSpecimenLabelSettings(specimen.getCpId());
		if (labelSettings == null || labelSettings.isEmpty()) {
			return null;
		}

		List<Map<String, String>> rules = (List<Map<String, String>>) labelSettings.get("rules");
		if (rules == null || rules.isEmpty()) {
			return null;
		}

		SpecimenDetail detail = SpecimenDetail.from(specimen, false, false, true);
		if (detail.getExtensionDetail() == null && specimen.getExtension() != null) {
			detail.setExtensionDetail(ExtensionDetail.from(specimen.getExtension(), false, true));
		}

		Map<String, Object> ctxt = new HashMap<>();
		ctxt.put("specimen", detail);
		ctxt.put("cpId", specimen.getCpId());

		for (Map<String, String> rule : rules) {
			initRuleVariables(rule.get("init"), ctxt);

			String criteria = rule.get("criteria");
			if (matches(criteria, ctxt)) {
				return rule.get("format");
			}
		}

		return null;
	}

	private static Map<String, Object> getSpecimenLabelSettings(Long cpId) {
		return CpWorkflowTxnCache.getInstance().getValue(cpId, "labelSettings", "specimen");
	}

	private static void initRuleVariables(String initExpr, Map<String, Object> ctxt) {
		if (StringUtils.isNotBlank(initExpr)) {
			ExpressionUtil.getInstance().evaluate(initExpr, ctxt);
		}
	}

	private static boolean matches(String criteria, Map<String, Object> ctxt) {
		if (StringUtils.isBlank(criteria)) {
			return true;
		}

		Object value = ExpressionUtil.getInstance().evaluate(criteria, ctxt);
		return (value instanceof Boolean) && (Boolean) value;
	}
}
