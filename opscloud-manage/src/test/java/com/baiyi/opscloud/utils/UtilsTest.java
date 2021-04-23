package com.baiyi.opscloud.utils;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.RandomUtils;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2020/11/2 9:56 上午
 * @Version 1.0
 */
public class UtilsTest extends BaseUnit {

    @Test
    void test() {
        for (int i = 1; i <= 10; i++) {
            System.err.println(RandomUtils.acqRandom(1));
        }
    }

}

