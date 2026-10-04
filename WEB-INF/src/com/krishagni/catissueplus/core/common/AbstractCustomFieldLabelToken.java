package com.krishagni.catissueplus.core.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseExtensionEntity;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.errors.CommonErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.de.domain.DeObject;

public abstract class AbstractCustomFieldLabelToken extends AbstractLabelTmplToken {
	@Override
	public String getName() {
		return "CUSTOM_FIELD";
	}

	@Override
	public String getReplacement(Object object) {
		throw new IllegalArgumentException("Invalid number of input parameters. Required level and field names");
	}

	@Override
	public String getReplacement(Object object, String ... args) {
		if (args == null || args.length != 2) {
			throw new IllegalArgumentException("Invalid number of input parameters. Required level and field names");
		}

		String level = args[0];
		if (StringUtils.isBlank(level)) {
			throw OpenSpecimenException.userError(CommonErrorCode.CUSTOM_FIELD_LEVEL_REQ);
		}

		String fieldName = args[1];
		if (StringUtils.isBlank(fieldName)) {
			throw OpenSpecimenException.userError(CommonErrorCode.CUSTOM_FIELD_NAME_REQ);
		}

		level = level.trim();
		fieldName = fieldName.trim();
		BaseExtensionEntity extensionEntity = getObject(object, level);
		if (extensionEntity == null) {
			return StringUtils.EMPTY;
		}

		BeanWrapper extnWrapper = PropertyAccessorFactory.forBeanPropertyAccess(extensionEntity);
		DeObject extn = (DeObject)extnWrapper.getPropertyValue("extension");
		if (extn == null) {
			return StringUtils.EMPTY;
		}

		String value = null;
		for (DeObject.Attr attr : extn.getAttrs()) {
			if (attr.getUdn().equals(fieldName)) {
				value = attr.getDisplayValue();
				break;
			}
		}

		if (StringUtils.isBlank(value)) {
			return StringUtils.EMPTY;
		} else {
			return value.trim();
		}
	}

	protected abstract BaseExtensionEntity getObject(Object object, String level);
}
