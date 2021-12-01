package com.baiyi.opscloud.common.util;

import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2021/12/1 1:34 下午
 * @Version 1.0
 */
public class EmailUtil {

    private EmailUtil(){}

    public static String toUsername(String email) {
        if (StringUtils.isEmpty(email))
            return "";
        if (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
            return email;
        }else{
            int atIndex = email.indexOf("@");
            return email.substring(0,atIndex);
        }
    }
}
