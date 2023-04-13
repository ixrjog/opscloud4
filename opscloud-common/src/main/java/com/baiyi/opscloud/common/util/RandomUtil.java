package com.baiyi.opscloud.common.util;

import java.util.Random;

/**
 * @Author baiyi
 * @Date 2022/1/4 6:42 PM
 * @Version 1.0
 */
public class RandomUtil {

    private RandomUtil() {
    }

    public static int random(int i) {
        Random r = new Random();
        return r.nextInt(i);
    }

}