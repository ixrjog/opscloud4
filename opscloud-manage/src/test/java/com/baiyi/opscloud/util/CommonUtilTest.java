package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import org.junit.jupiter.api.Test;
import com.baiyi.opscloud.common.util.IPAddressUtil;

/**
 * @Author baiyi
 * @Date 2022/3/15 11:49 AM
 * @Version 1.0
 */
public class CommonUtilTest extends BaseUnit {

    @Test
    void iPAddressUtilTest() {
        print(IPAddressUtil.isIPv4LiteralAddress("192.168.1.1"));
    }

}
