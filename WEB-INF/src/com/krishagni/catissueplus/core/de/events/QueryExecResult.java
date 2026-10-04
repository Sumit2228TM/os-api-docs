package com.krishagni.catissueplus.core.de.events;

import java.util.List;
import java.util.Map;

public class QueryExecResult {
	private List<Map<String, Object>> columnMetadata;

	private String[] columnLabels;

	private String[] columnTypes;

	private String[] columnUrls;
	
	private List<String[]> rows;
	
	private Integer[] columnIndices;
	
	private int dbRowsCount;

	public List<Map<String, Object>> getColumnMetadata() {
		return columnMetadata;
	}

	public QueryExecResult setColumnMetadata(List<Map<String, Object>> columnMetadata) {
		this.columnMetadata = columnMetadata;
		return this;
	}

	public String[] getColumnLabels() {
		return columnLabels;
	}

	public QueryExecResult setColumnLabels(String[] columnLabels) {
		this.columnLabels = columnLabels;
		return this;
	}

	public String[] getColumnTypes() {
		return columnTypes;
	}

	public QueryExecResult setColumnTypes(String[] columnTypes) {
		this.columnTypes = columnTypes;
		return this;
	}

	public String[] getColumnUrls() {
		return columnUrls;
	}

	public QueryExecResult setColumnUrls(String[] columnUrls) {
		this.columnUrls = columnUrls;
		return this;
	}

	public List<String[]> getRows() {
		return rows;
	}

	public QueryExecResult setRows(List<String[]> rows) {
		this.rows = rows;
		return this;
	}
	
	public Integer[] getColumnIndices() {
		return columnIndices;
	}

	public QueryExecResult setColumnIndices(Integer[] columnIndices) {
		this.columnIndices = columnIndices;
		return this;
	}

	public int getDbRowsCount() {
		return dbRowsCount;
	}

	public QueryExecResult setDbRowsCount(int dbRowsCount) {
		this.dbRowsCount = dbRowsCount;
		return this;
	}

	public static QueryExecResult create(String[] labels, List<String[]> rows, int dbRowsCount, Integer[] indices) {
		QueryExecResult resp = new QueryExecResult();
		resp.setColumnLabels(labels);
		resp.setRows(rows);
		resp.setDbRowsCount(dbRowsCount);
		resp.setColumnIndices(indices);	
		return resp;
	}
	
}