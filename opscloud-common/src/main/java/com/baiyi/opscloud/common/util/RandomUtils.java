package com.baiyi.opscloud.common.util;

/**
 * @Author baiyi
 * @Date 2020/11/2 9:49 上午
 * @Version 1.0
 */
public class RandomUtils {

    public static long acqRandom(int second) {
        double d = Math.random();
        return (long) (second * d * 1000);
    }

}
