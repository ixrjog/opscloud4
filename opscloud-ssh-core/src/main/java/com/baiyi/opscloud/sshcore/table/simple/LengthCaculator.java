/*$Id: $
 * author   date   comment
 * cwjcsu@gmail.com  2016年5月19日  Created
 */
package com.baiyi.opscloud.sshcore.table.simple;

/**
 * @author liangjian
 */
public interface LengthCaculator {

    int charWidth(char ch);

    int length(String str);

    String substring(String string, int start, int end);

    String substring(String string, int beginIndex);

}
