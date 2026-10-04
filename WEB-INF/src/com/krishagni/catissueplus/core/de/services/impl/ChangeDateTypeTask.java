package com.krishagni.catissueplus.core.de.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CommonErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;

public class ChangeDateTypeTask implements ScheduledTask {

	private static final String ALTER_COLUMN_TYPE = "ALTER TABLE %s CHANGE %s %s DATETIME";

	@Override
	@PlusTransactional
	public void doJob(ScheduledJobRun run)
	throws Exception {
		String args = run.getRtArgs();
		String[] formInfo = args.split("\\s+");
		if (formInfo.length < 2) {
			throw error( "Form and field names are mandatory");
		}

		Container form = Container.getContainer(formInfo[0].trim());
		if (form == null) {
			throw error( "Invalid form name: " + formInfo[0].trim());
		}

		Control field = form.getControlByUdn(formInfo[1].trim(), "\\.");
		if (field == null) {
			throw error( "Invalid field name: " + formInfo[1].trim());
		}

		if (!(field instanceof DatePicker)) {
			throw error("Field " + formInfo[1].trim() + " is not a date field!");
		}

		String tableName  = field.getContainer().getDbTableName();
		String columnName = field.getDbColumnName();
		JdbcDaoFactory.getJdbcDao().executeDDL(String.format(ALTER_COLUMN_TYPE, tableName, columnName, columnName));
	}

	private OpenSpecimenException error(String msg) {
		return OpenSpecimenException.userError(CommonErrorCode.INVALID_INPUT, msg);
	}
}
