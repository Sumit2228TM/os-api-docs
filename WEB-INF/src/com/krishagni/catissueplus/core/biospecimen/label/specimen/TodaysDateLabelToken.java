package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;

public class TodaysDateLabelToken extends AbstractSpecimenLabelToken {

	public TodaysDateLabelToken() {
		this.name = "TODAY_DATE";
	}

	@Override
	public String getLabelN(Specimen specimen, String ...args) {
		String timestampFormat = null;
		if (args != null) {
			timestampFormat = args[0];
		}

		return StringUtils.isEmpty(timestampFormat) ? getLabel(specimen) : getCurrentTimeStamp(timestampFormat); 
	}

	@Override
	public String getLabel(Specimen specimen) {
		return getCurrentTimeStamp(ConfigUtil.getInstance().getDeDateFmt());
	}

	private String getCurrentTimeStamp(String timestampFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(timestampFormat);
		return sdf.format(Calendar.getInstance().getTime());
	}
}
