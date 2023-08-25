package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2023/8/11 18:05
 * @Version 1.0
 */
public class SystemUtilsTest extends BaseUnit {

    @Test
    void test() {
        print(SystemUtils.getHostName());
        print(SystemUtils.JAVA_VERSION);
        print(SystemUtils.OS_NAME);
        print(SystemUtils.USER_TIMEZONE);
        print(SystemUtils.USER_NAME);
    }

}
