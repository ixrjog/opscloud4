package com.baiyi.caesar.util;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.util.IdUtil;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2021/5/18 5:28 下午
 * @Version 1.0
 */
public class IdUtilTest extends BaseUnit {

    @Test
    void buildUUIDTest() {
        System.err.println(IdUtil.buildUUID());
    }
}
