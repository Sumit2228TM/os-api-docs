package com.krishagni.catissueplus.core.de.services.impl;

import java.io.File;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;
import com.krishagni.catissueplus.core.common.events.BulkDeleteEntityOp;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.CsvFileWriter;
import com.krishagni.catissueplus.core.common.util.CsvWriter;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.common.dynamicextensions.domain.nui.Container;

@Configurable
public class DeleteFormsTask implements ScheduledTask {

	@Autowired
	private FormService formSvc;


	@Override
	public void doJob(ScheduledJobRun jobRun) throws Exception {
		File outFile = new File(
			ConfigUtil.getInstance().getDataDir() + File.separator + "logs",
			"delete_forms_" + jobRun.getId() + ".csv"
		);
		CsvWriter writer = CsvFileWriter.createCsvFileWriter(outFile);
		jobRun.setLogFilePath(outFile.getAbsolutePath());


		try {
			writer.writeNext(new String[] {"Form Name", "Status", "Error Message", "Exception"});

			String args = jobRun.getRtArgs();
			if (args == null || args.trim().isEmpty()) {
				return;
			}

			Set<String> formNames = new LinkedHashSet<>(Utility.csvToStringList(args));
			for (String formName : formNames) {
				try {
					Container form = Container.getContainer(formName);
					if (form == null) {
						logStatus(writer, formName, "FAILED", "Form does not exist", null);
						continue;
					}

					Boolean status = response(formSvc.deleteForms(request(formDeleteOp(form.getId()))));
					if (Boolean.TRUE.equals(status)) {
						logStatus(writer, formName, "SUCCESS", null, null);
					} else {
						logStatus(writer, formName, "FAILED", "Failed to delete the form. Check logs for more details.", null);
					}
				} catch (Throwable t) {
					logStatus(writer, formName, "FAILED", ExceptionUtils.getRootCauseMessage(t), t);
				}
			}
		} catch (Throwable t) {
			logStatus(writer, "Rest of the forms", "FAILED", ExceptionUtils.getRootCauseMessage(t), t);
		} finally {
			try {
				writer.flush();
			} finally {
				IOUtils.closeQuietly(writer);
			}
		}
	}

	private BulkDeleteEntityOp formDeleteOp(Long formId) {
		BulkDeleteEntityOp op = new BulkDeleteEntityOp();
		op.setIds(Collections.singleton(formId));
		return op;
	}

	private void logStatus(CsvWriter writer, String form, String status, String message, Throwable t) {
		writer.writeNext(new String[] { form, status, message, t != null ? ExceptionUtils.getStackTrace(t) : null });
	}

	private <T> RequestEvent<T> request(T payload) {
		return new RequestEvent<>(payload);
	}

	private <T> T response(ResponseEvent<T> resp) {
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
}