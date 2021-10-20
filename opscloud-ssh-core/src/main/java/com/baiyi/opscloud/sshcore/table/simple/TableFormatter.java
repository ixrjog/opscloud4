package com.baiyi.opscloud.sshcore.table.simple;

import java.io.IOException;

/**
 * 表格数据的格式化
 * @author atlas
 * @date 2012-12-5
 */
public interface TableFormatter {
	/**
	 * @deprecated may cause OOM
	 * 
	 * @param table
	 * @return
	 */
	String format(Table table);

	/**
	 * 格式化表格数据并输出到Appendable对象上面
	 * 
	 * @param table
	 * @param writer
	 * @throws IOException
	 */
	void format(Table table, Appendable writer) throws IOException;
}
