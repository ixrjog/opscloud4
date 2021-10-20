package com.baiyi.opscloud.sshcore.table.simple;

import java.util.List;

/**
 * 需要展示的数据
 * @author atlas
 * @date 2012-12-5
 */
public interface Table {
	/**
	 * 表格标题
	 * @return
	 */
	String getTitle();

	/**
	 * 表格每列的头部
	 * @return
	 */
	String[] getHeaders();

	/**
	 * 表格具有的每行数据
	 * @return
	 */
	List<Row> getRows();

}
