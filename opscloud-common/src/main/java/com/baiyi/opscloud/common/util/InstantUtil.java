package com.baiyi.opscloud.common.util;

import java.time.Duration;
import java.time.Instant;

/**
 * @Author baiyi
 * @Date 2021/4/2 10:21 上午
 * @Version 1.0
 */
public class InstantUtil {

    private InstantUtil() {
    }

    public static long timerMillis(Instant inst) {
        return Instant.now().toEpochMilli() - inst.toEpochMilli();
    }

    public static long timerSeconds(Instant inst) {
        return Duration.between(inst, Instant.now()).getSeconds();
    }

}