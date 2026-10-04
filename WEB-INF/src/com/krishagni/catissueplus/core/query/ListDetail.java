package com.krishagni.catissueplus.core.query;

import java.util.ArrayList;
import java.util.List;

public class ListDetail {
	private String icons;

	private List<Column> columns = new ArrayList<>();

	private List<Column> fixedColumns;

	private List<Row> rows = new ArrayList<>();

	private int size = -1;

	public String getIcons() {
		return icons;
	}

	public void setIcons(String icons) {
		this.icons = icons;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public List<Column> getFixedColumns() {
		return fixedColumns;
	}

	public void setFixedColumns(List<Column> fixedColumns) {
		this.fixedColumns = fixedColumns;
	}

	public List<Row> getRows() {
		return rows;
	}

	public void setRows(List<Row> rows) {
		this.rows = rows;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
