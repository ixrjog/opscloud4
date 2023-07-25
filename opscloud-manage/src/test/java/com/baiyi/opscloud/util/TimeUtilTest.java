package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2023/7/25 11:48
 * @Version 1.0
 */
public class TimeUtilTest extends BaseUnit {



    @Test
    void timeTest(){
        Date now = new Date();

        NewTimeUtil.millisecondsSleep(5000L);

        print(System.currentTimeMillis());
        print(now.getTime());
        print(System.currentTimeMillis() - now.getTime());
    }
}
