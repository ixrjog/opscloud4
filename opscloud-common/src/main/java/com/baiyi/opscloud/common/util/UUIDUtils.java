package com.baiyi.opscloud.common.util;

import java.util.UUID;

/**
 * @Author baiyi
 * @Date 2020/2/13 10:20 上午
 * @Version 1.0
 */
public class UUIDUtils {


    /**
     * 获得一个UUID   不含-
     * @return String UUID
     */
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

}
