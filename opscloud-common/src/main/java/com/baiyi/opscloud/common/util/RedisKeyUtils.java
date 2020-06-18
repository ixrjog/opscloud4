package com.baiyi.opscloud.common.util;

import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2020/4/7 6:55 下午
 * @Version 1.0
 */
public class RedisKeyUtils {

    public static String getMyServerTreeKey(int userId,String uuid){
        return Joiner.on(":").join("serverTree", "userId", userId, "uuid", uuid);
    }

}
