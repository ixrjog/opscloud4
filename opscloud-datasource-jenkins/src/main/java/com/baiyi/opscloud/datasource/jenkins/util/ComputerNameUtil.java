package com.baiyi.opscloud.datasource.jenkins.util;

import com.baiyi.opscloud.datasource.jenkins.client.util.EncodingUtils;

/**
 * @Author baiyi
 * @Date 2022/4/6 14:49
 * @Version 1.0
 */
public class ComputerNameUtil {

    private static final String BUILT_IN_NODE_NAME = "built-in node";

    public static String toName(String displayName) {
        return BUILT_IN_NODE_NAME.equalsIgnoreCase(displayName) ? "(built-in)" : EncodingUtils.encode(displayName);
    }

}