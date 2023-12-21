package com.baiyi.opscloud.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author baiyi
 * @Date 2023/12/21 09:36
 * @Version 1.0
 */
public class MatchingUtil {

    public static boolean fuzzyMatching(String text, String fuzzyStr) {
        if (StringUtils.isEmpty(text)) return false;
        if (StringUtils.isEmpty(fuzzyStr)) return true;
        // 不含通配符的字符串
        final String findStr = fuzzyStr.replace("*", "");
        // 无通配符
        if (findStr.equals(fuzzyStr)) {
            return text.equals(fuzzyStr);
        }
        // 模糊匹配
        // *在前, 后缀匹配
        if (fuzzyStr.startsWith("*")) {
            return text.endsWith(findStr);
        }
        // *在后, 前缀匹配
        if (fuzzyStr.endsWith("*")) {
            return text.startsWith(findStr);
        }
        return false;
    }

}