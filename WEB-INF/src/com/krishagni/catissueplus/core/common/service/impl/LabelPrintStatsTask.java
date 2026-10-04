package com.krishagni.catissueplus.core.common.service.impl;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CommonErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.LabelPrintStat;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.CsvFileWriter;
import com.krishagni.catissueplus.core.common.util.MessageUtil;
import com.krishagni.catissueplus.core.common.util.Utility;

@Configurable
public class LabelPrintStatsTask implements ScheduledTask {
	@Autowired
	private DaoFactory daoFactory;

	@Override
	public void doJob(ScheduledJobRun jobRun)
	throws Exception {
		Pair<Date, Date> dateRange = getDateRange(jobRun);
		List<LabelPrintStat> stats = getPrintStats(dateRange.first(), dateRange.second());
		File statsCsv = exportToCsv(jobRun.getId(), dateRange.first(), dateRange.second(), stats);
		jobRun.setLogFilePath(statsCsv.getAbsolutePath());
	}

	private Pair<Date, Date> getDateRange(ScheduledJobRun jobRun) {
		String args = jobRun.getRtArgs();
		if (StringUtils.isBlank(args)) {
			return getPreviousCalendarMonth();
		} else {
			String[] dates = args.split(",|\\s+");
			Date start = getDate(dates[0]);
			Date end = dates.length > 1 ? getDate(dates[1]) : Utility.getEndOfDay(Calendar.getInstance().getTime());
			return Pair.make(start, end);
		}
	}

	private Pair<Date, Date> getPreviousCalendarMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DATE, 1);
		Date start = Utility.chopTime(cal.getTime());

		cal.setTime(start);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.SECOND, -1);
		Date end = cal.getTime();
		return Pair.make(start, end);
	}

	private Date getDate(String input) {
		try {
			return new SimpleDateFormat(ConfigUtil.getInstance().getDeDateFmt()).parse(input);
		} catch (ParseException pe) {
			throw OpenSpecimenException.userError(CommonErrorCode.INVALID_INPUT, input);
		}
	}

	@PlusTransactional
	private List<LabelPrintStat> getPrintStats(Date start, Date end) {
		return daoFactory.getLabelPrintJobDao().getPrintStats("specimen", start, end);
	}

	private File exportToCsv(Long runId, Date start, Date end, List<LabelPrintStat> stats) {
		CsvFileWriter writer = null;
		try {
			File outputFile = new File(ConfigUtil.getInstance().getReportsDir(), "printed_labels_count_" + runId + ".csv");
			writer = CsvFileWriter.createCsvFileWriter(outputFile);

			writer.writeNext(new String[] {msg("common_exported_by"), AuthUtil.getCurrentUser().formattedName()});
			writer.writeNext(new String[] {msg("common_exported_on"), Utility.getDateString(Calendar.getInstance().getTime())});
			writer.writeNext(new String[] {msg("label_print_stat_start_date"), Utility.getDateString(start)});
			writer.writeNext(new String[] {msg("label_print_stat_end_date"), Utility.getDateString(end)});
			writer.writeNext(new String[0]);

			writer.writeNext(new String[] {
				msg("label_print_stat_site"),
				msg("label_print_stat_cp"),
				msg("label_print_stat_user_fname"),
				msg("label_print_stat_user_lname"),
				msg("label_print_stat_user_email"),
				msg("label_print_stat_count")
			});

			for (LabelPrintStat stat : stats) {
				writer.writeNext(toArray(stat));
			}

			return outputFile;
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	private String msg(String key) {
		return MessageUtil.getInstance().getMessage(key);
	}

	private String[] toArray(LabelPrintStat stat) {
		return new String[] {
			stat.getSite(),
			stat.getProtocol(),
			stat.getUserFirstName(),
			stat.getUserLastName(),
			stat.getUserEmailAddress(),
			String.valueOf(stat.getCount())
		};
	}
}
