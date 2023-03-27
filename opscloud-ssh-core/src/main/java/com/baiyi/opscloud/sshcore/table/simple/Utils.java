/*$Id: $
 * author   date   comment
 * cwjcsu@gmail.com  2016年5月19日  Created
*/
package com.baiyi.opscloud.sshcore.table.simple;

import java.util.Optional;

public class Utils {

	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	public static String repeat(char ch, int count) {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < count; ++i) {
            buffer.append(ch);
        }
		return buffer.toString();
	}

	public static int sum(int[] d) {
		int sum = 0;
		for (int d0 : d) {
			sum += d0;
		}
		return sum;
	}
	
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
    
    public static boolean isChChar(char ch) {
        return ch > 255 || ch < 0;
    }

    /**
     * 截取一段子串（从start字节到end字节，假设一个ascii字符1字节，一个非ascii字符2个字节），子串的字节长度为end-start
     *
     * @param string
     * @param start
     * @param end
     * @return
     */
    public static String subCHString(String string, int start, int end) {
        if (string != null) {
            int len = 0;
            StringBuilder strs = new StringBuilder();
            int D = 0;
            for (int i = 0; i < string.length(); i++) {
                int d = isChChar(string.charAt(i)) ? 2 : 1;
                len += d;
                if (end < len || D >= (end - start))
                    return strs.toString();
                else if (start < len) {
                    strs.append(string.charAt(i));
                    D += d;
                }
            }
            return new String(strs);
        }
        return Optional.ofNullable(string).map(s -> s.substring(start, end)).orElse(null);
    }

    public static String subCHString(String string, int start) {
        return subCHString(string, start, Integer.MAX_VALUE);
    }

    /**
     * 截取字符串（从start字节截取length个字节,假设一个ascii字符1字节，一个非ascii字符2个字节）
     *
     * @param string
     * @param start
     * @param length
     * @return
     */
    public static String subCHStringByLenth(String string, int start, int length) {
        return subCHString(string, start, start + length);
    }

    public static int lengthCH(String string) {
        if (string != null) {
            int len = 0;
            for (int i = 0; i < string.length(); i++) {
                if (isChChar(string.charAt(i))) {
                    len += 2;
                } else {
                    len++;
                }
            }
            return len;
        }
        return 0;
    }

}
