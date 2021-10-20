/*$Id: $
 * author   date   comment
 * cwjcsu@gmail.com  2016年5月19日  Created
*/
package com.baiyi.opscloud.sshcore.table.simple;

public class AscciiLengthCaculator implements LengthCaculator {
 
	public int charWidth(char ch) {
		return 1;
	}

	public int length(String str) {
		return str != null ? str.length() : 0;
	}

	public String substring(String string, int start, int end) {
		return string;
	}

	public String substring(String string, int beginIndex) {
		if (string != null) {
			return string.substring(beginIndex);
		}
		return null;
	}

}
