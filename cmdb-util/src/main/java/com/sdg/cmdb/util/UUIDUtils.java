package com.sdg.cmdb.util;


import java.util.UUID;

public class UUIDUtils {

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 返回的UUID不包含-
     *
     * @return
     */
    public static String getUUIDByShort() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    /**
     * 短格式转标准格式
     *
     * @param uuid
     * @return
     */
    public static String convertUUID(String uuid) {
        if (uuid.length() != 32) return uuid;
        return uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32);
    }
}
