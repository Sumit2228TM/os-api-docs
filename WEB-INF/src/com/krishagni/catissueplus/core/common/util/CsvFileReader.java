package com.krishagni.catissueplus.core.common.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

public class CsvFileReader implements CsvReader {
	private Map<String, Integer> columnNameIdxMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	private String[] currentRow;

	private CSVReader csvReader;

	private boolean firstRowHeaderRow;

	public CsvFileReader(CSVReader csvReader, boolean firstRowHeaderRow) {
		this.csvReader = csvReader;
		this.firstRowHeaderRow = firstRowHeaderRow;
		if (firstRowHeaderRow) {
			createColumnNameIdxMap();
		}
	}

	public static CsvFileReader createCsvFileReader(InputStream inputStream, boolean firstRowHeaderRow) {
		return createCsvFileReader(inputStream, firstRowHeaderRow, Utility.getFieldSeparator());
	}

	public static CsvFileReader createCsvFileReader(InputStream in, boolean firstRowHeader, char separator) {
		return createCsvFileReader(new InputStreamReader(in), firstRowHeader, separator);
	}

	public static CsvFileReader createCsvFileReader(Reader reader, boolean firstRowHeaderRow) {
		return createCsvFileReader(reader, firstRowHeaderRow, Utility.getFieldSeparator());
	}

	public static CsvFileReader createCsvFileReader(Reader reader, boolean firstRowHeaderRow, char separator) {
		CSVReader csvReader = new CSVReader(reader, separator);
		return new CsvFileReader(csvReader, firstRowHeaderRow);
	}
	
	public static CsvFileReader createCsvFileReader(String csvFile, boolean firstRowHeaderRow) {
		return createCsvFileReader(csvFile, firstRowHeaderRow, Utility.getFieldSeparator());
	}

	public static CsvFileReader createCsvFileReader(String csvFile, boolean firstRowHeaderRow, char separator) {
		FileReader fr = null;
		try {
			fr = new FileReader(csvFile);
			CSVReader csvReader = new CSVReader(fr, separator);
			return new CsvFileReader(csvReader, firstRowHeaderRow);
		} catch (IOException e) {
			IOUtils.closeQuietly(fr);
			throw new CsvException("Error creating CSV file reader", e);
		}
	}

	public static int getRowsCount(String csvFile, boolean firstRowHeaderRow) {
		CsvFileReader reader = null;
		try {
			reader = createCsvFileReader(csvFile, firstRowHeaderRow);

			int count = 0;
			while (reader.next()) {
				count++;
			}
			return count;
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	public String[] getColumnNames() {
		String[] columnNames = new String[columnNameIdxMap.size()];
		for (Map.Entry<String, Integer> columnNameIdx : columnNameIdxMap.entrySet()) {
			columnNames[columnNameIdx.getValue()] = columnNameIdx.getKey();
		}

		return columnNames;
	}

	public boolean isColumnPresent(String columnName) {
		return getColumnIdx(columnName) >= 0;
	}

	public String getColumn(String columnName) {
		int columnIdx = getColumnIdx(columnName);
		if (columnIdx == -1) {
			return null;
		}

		return getColumn(columnIdx);
	}

	public String getColumn(int columnIndex) {
		if (currentRow == null) {
			throw new CsvException(
					"Programming error. Current row not initialised. Call next()");
		}

		if (columnIndex < 0 || columnIndex >= currentRow.length) {
			throw new CsvException("Invalid column index: " + columnIndex);
		}

		return currentRow[columnIndex];
	}

	public String[] getRow() {
		return currentRow;
	}

	public boolean next() {
		try {
			currentRow = csvReader.readNext();
			if (currentRow == null || currentRow.length == 0) {
				return false;
			}

			for (int i = 0; i < currentRow.length; i++) {
				String col = currentRow[i] = StringUtils.trim(currentRow[i]);
				if (col != null && col.length() > 1 && col.charAt(0) == '\'' && UNSAFE_CHARS.indexOf(col.charAt(1)) > -1) {
					currentRow[i] = col.substring(1);
				}
			}

			return true;
		} catch (IOException e) {
			throw new CsvException("Error reading line from CSV file", e);
		}
	}

	public void close() {
		try {
			csvReader.close();
		} catch (IOException e) {
			throw new CsvException("Error closing CSVReader", e);
		}
	}

	private void createColumnNameIdxMap() {
		try {
			String[] line = null;
			while ((line = csvReader.readNext()) != null && line.length > 0 && line[0].startsWith("#"))
				;

			if (line == null || line.length == 0) {
				throw new CsvException("CSV file column names line empty");
			}

			for (int i = 0; i < line.length; ++i) {
				String column = line[i] != null ? line[i].trim() : null;
				if (StringUtils.isBlank(column)) {
					throw new CsvException("CSV file column names line has empty/blank column names", line);
				} else if (columnNameIdxMap.containsKey(column)) {
					throw new CsvException("CSV file column names are duplicate: " + column, line);
				}

				columnNameIdxMap.put(column, i);
			}
		} catch (IOException e) {
			throw new CsvException("Error reading CSV file column names line", e);
		}
	}

	private int getColumnIdx(String columnName) {
		if (!firstRowHeaderRow) {
			throw new CsvException(
					"CSV file reader created without first row column names");
		}

		Integer columnIdx = columnNameIdxMap.get(columnName.trim());
		return columnIdx == null ? -1 : columnIdx;
	}

	private static final String UNSAFE_CHARS = ";=@+-";
}
