package com.baiyi.opscloud.common.util;

import com.google.common.base.Joiner;

import java.util.UUID;

/**
 * @Author baiyi
 * @Date 2020/2/13 10:20 上午
 * @Version 1.0
 */
public class UUIDUtils {


    /**
     * 获得一个UUID   不含-
     *
     * @return String UUID
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    /**
     * 短格式转标准格式
     *
     * @param uuid
     * @return
     */
    public static String convertUUID(String uuid) {
        if (uuid.length() != 32) return uuid;
        return Joiner.on("-").join(uuid.substring(0, 8), uuid.substring(8, 12), uuid.substring(12, 16), uuid.substring(16, 20), uuid.substring(20, 32));
    }

}
