/*$Id: $
 * author   date   comment
 * cwjcsu@gmail.com  2016年5月19日  Created
*/
package com.baiyi.opscloud.sshcore.table.simple;

import java.util.Optional;

public class DefaultLengthCaculator implements LengthCaculator {

	public boolean isChineseChar(char ch) {
		return ch > 255 || ch < 0;
	}

	public int charWidth(char ch) {
		if (isChineseChar(ch)) {
			return 2;
		}
		return 1;
	}

	public int length(String string) {
		if (string != null) {
			int len = 0;
			for (int i = 0; i < string.length(); i++) {
				len += charWidth(string.charAt(i));
			}
			return len;
		}
		return 0;
	}

	/**
	 * 截取一段子串（从start字节到end字节，假设一个ascii字符1字节，一个非ascii字符2个字节），子串的字节长度为end-start
	 *
	 * @param string
	 * @param start
	 * @param end
	 * @return
	 */
	public String substring(String string, int start, int end) {
		if (string != null) {
			int len = 0;
			StringBuilder strs = new StringBuilder();
			int D = 0;
			for (int i = 0; i < string.length(); i++) {
				int d = charWidth(string.charAt(i));
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

	public String substring(String string, int beginIndex) {
		return substring(string, beginIndex, Integer.MAX_VALUE);
	}

}
