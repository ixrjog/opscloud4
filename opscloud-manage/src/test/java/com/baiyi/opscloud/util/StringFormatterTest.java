package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.StringFormatter;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2023/7/14 13:28
 * @Version 1.0
 */
public class StringFormatterTest extends BaseUnit {

    @Test
    void test() {
        print(StringFormatter.format("A1={}", "BBB"));
        print(StringFormatter.format("A2={}", 1L));
        print(StringFormatter.arrayFormat("B1={},B2={},B3={}", 1L, 100L, "TEST"));
    }

}
