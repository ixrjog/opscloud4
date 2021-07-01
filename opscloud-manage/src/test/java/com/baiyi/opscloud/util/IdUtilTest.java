package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.IdUtil;
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
