package com.baiyi.opscloud.common.util;

import com.google.common.base.Joiner;

import java.util.UUID;

/**
 * @Author baiyi
 * @Date 2020/3/25 10:16 上午
 * @Version 1.0
 */
public class IdUtil {

    /**
     * 判断id是否为空
     *
     * @param id
     * @return
     */
    public static boolean isEmpty(Integer id) {
        return id == null || id <= 0;
    }

    public static boolean isNotEmpty(Integer id) {
        return id != null;
    }

    /**
     * 获得一个UUID   不含-
     *
     * @return String UUID
     *//**/
    public static String buildUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    /**
     * 短格式转标准格式
     *
     * @param uuid 10c0efa8a0fa41a89f6387c363e3d0e5
     * @return 10c0efa8-a0fa-41a8-9f63-87c363e3d0e5
     */
    public static String toUUID(String uuid) {
        if (uuid.length() != 32) {
            return uuid;
        }
        return Joiner.on("-").join(uuid.substring(0, 8), uuid.substring(8, 12), uuid.substring(12, 16), uuid.substring(16, 20), uuid.substring(20, 32));
    }

}