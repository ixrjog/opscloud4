package com.baiyi.opscloud.common.util;

/**
 * @Author baiyi
 * @Date 2020/3/25 10:16 上午
 * @Version 1.0
 */
public class IDUtils {

    /**
     * 判断id是否为空
     * @param id
     * @return
     */
    public static boolean isEmpty(Integer id) {
        return id == null || id <= 0;
    }
}
