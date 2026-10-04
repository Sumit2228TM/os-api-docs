
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.ParticipantMedicalIdentifier;
import com.krishagni.catissueplus.core.common.util.Utility;

public class PmiDetail {

	String siteName;

	String mrn;

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getMrn() {
		return mrn;
	}

	public void setMrn(String mrn) {
		this.mrn = mrn;
	}

	public String toString() {
		String result = StringUtils.isNotBlank(getSiteName()) ? getSiteName() : "Not Specified";
		if (StringUtils.isNotBlank(getMrn())) {
			result += " (" + getMrn() + ")";
		}

		return result;
	}
	
	public static PmiDetail from(ParticipantMedicalIdentifier pmi, boolean excludePhi) {
		PmiDetail result = new PmiDetail();
		result.setMrn(excludePhi ? "###" : pmi.getMedicalRecordNumber());
		result.setSiteName(pmi.getSite().getName());
		return result;
	}
	
	public static List<PmiDetail> from(Collection<ParticipantMedicalIdentifier> pmis, boolean excludePhi) {
		return pmis.stream().map(pmi -> PmiDetail.from(pmi, excludePhi)).collect(Collectors.toList());
	}

	public static String toString(Collection<PmiDetail> pmis) {
		return Utility.nullSafeStream(pmis).map(PmiDetail::toString).collect(Collectors.joining(", "));
	}

	public static Map<String, String> toMap(List<PmiDetail> pmis) {
		Map<String, String> result = new HashMap<>();
		for (PmiDetail pmi : pmis) {
			if (StringUtils.isBlank(pmi.getSiteName())) {
				continue;
			}

			result.put(pmi.getSiteName().toLowerCase(), pmi.getMrn() != null ? pmi.getMrn().toLowerCase() : null);
		}

		return result;
	}
}
