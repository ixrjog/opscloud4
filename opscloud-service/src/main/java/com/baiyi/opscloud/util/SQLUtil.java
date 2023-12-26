package com.baiyi.opscloud.util;

import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2021/5/11 10:42 上午
 * @Version 1.0
 */
public class SQLUtil {

    private SQLUtil() {
    }

    public static String toLike(String queryName) {
        return Joiner.on("").join("%", queryName, "%");
    }

}