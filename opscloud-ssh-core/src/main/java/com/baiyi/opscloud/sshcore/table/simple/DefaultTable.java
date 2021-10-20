package com.baiyi.opscloud.sshcore.table.simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 
 * @author atlas
 * @date 2012-12-5
 */
public class DefaultTable implements Table {
	private String title;
	private String[] headers;

	private Comparator<?> cellComparator;

	private List<Row> body = new ArrayList<Row>();

	public DefaultTable() {
	}

	public DefaultTable(String title, String... headers) {
		super();
		this.title = title;
		this.headers = headers;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setHeaders(String[] headers) {
		this.headers = headers;
	}

	public void setCellComparator(Comparator<?> cellComparator) {
		this.cellComparator = cellComparator;
	}

	Comparator<?> getCellComparator() {
		return cellComparator;
	}

	public Row addRow(Object[] row) {
		if (row.length > headers.length) {
			throw new IllegalArgumentException("row(" + row.length
					+ ") is longer than header(" + headers.length + ")");
		}
		Object[] row0 = new Object[headers.length];
		System.arraycopy(row, 0, row0, 0, row.length);
		Row r = new Row(row0, this);
		body.add(r);
		return r;
	}

	public Row addRow(Object[] row, Comparator<?> cellComparator) {
		Row r = addRow(row);
		return r;
	}

	public String[] getHeaders() {
		return headers;
	}

	public String getTitle() {
		return title;
	}

	public List<Row> getRows() {
		return Collections.unmodifiableList(this.body);
	}
}
