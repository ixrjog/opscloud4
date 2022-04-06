package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.IPRegionUtil;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2022/4/6 10:21
 * @Version 1.0
 */
public class IPRegionUtilTest extends BaseUnit {

    @Test
    void checkTest() {
        print(IPRegionUtil.isInRange("172.29.1.1", "172.29.0.0/16"));
        print(IPRegionUtil.isInRange("172.29.1.1", "192.0.0.0/8"));
        print(IPRegionUtil.isInRange("172.29.1.1", "0.0.0.0/0"));
        print(IPRegionUtil.isInRange("172.29.1.1", "0.0.0.0"));
    }
}
