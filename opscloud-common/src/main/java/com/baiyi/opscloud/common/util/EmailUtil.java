package com.baiyi.opscloud.common.util;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/12/1 1:34 下午
 * @Version 1.0
 */
public class EmailUtil {

    private EmailUtil() {
    }

    public static String toUsername(String email) {
        if (StringUtils.isEmpty(email)) {
            return "";
        }
        if (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
            return email;
        } else {
            int atIndex = email.indexOf("@");
            String emailUsername = email.substring(0, atIndex);
            return convert(emailUsername);
        }
    }

    /**
     * 转换邮箱名称 a.b to ba
     * @param emailUsername
     * @return
     */
    private static String convert(String emailUsername) {
        if (!emailUsername.contains(".")) {
            return emailUsername;
        }
        List<String> names = Splitter.on(".").splitToList(emailUsername);
        if (names.size() == 2) {
            return names.get(1) + names.get(0);
        }
        return "";
    }

}