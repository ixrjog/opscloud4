package com.baiyi.opscloud.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author baiyi
 * @Date 2020/4/16 8:20 下午
 * @Version 1.0
 */
public class AnsibleUtils {

    /**
     *
     * @param ansibleResult
     * @return '10.200.1.40 | CHANGED => '
     */
    public static String getResultHead(String ansibleResult) {
        /**    10.200.1.40 | CHANGED => {    **/
        try {
            Pattern pattern = Pattern.compile("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\ \\|\\ \\w+\\ =>\\ ");
            Matcher matcher = pattern.matcher(ansibleResult);
            if (matcher.find())
                return matcher.group(0);
        } catch (Exception e) {
        }
        return "";
    }

}
