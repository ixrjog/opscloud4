package com.sdg.cmdb.util;

import java.util.Random;

public class RandomUtil {


    /**
     * 范围 0-range
     * @param range
     * @return
     */
    public static int random(int range) {
        Random rand = new Random();
        return rand.nextInt(range + 1);
    }


    /**
     *  百分比成功率计算
     * @param successRate
     * @return
     */
    public static boolean success(int successRate) {
        int r = random(100);
        if ( successRate >= r)
            return true;
        return false;

    }


}
