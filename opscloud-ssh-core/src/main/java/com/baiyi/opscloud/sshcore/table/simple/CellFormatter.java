package com.baiyi.opscloud.sshcore.table.simple;

/**
 * 表格的每个单元格输出格式化类
 * @author atlas
 * @date 2012-12-12
 */
public interface CellFormatter {
	/**
	 * 格式化后的输出字符串
	 * @param cell
	 * @return
	 */
	String format(Object cell);
	/**
	 * @param cell 可能是null
	 * @return 是否可以格式化对象cell，
	 */
	boolean accepts(Object cell);
}
