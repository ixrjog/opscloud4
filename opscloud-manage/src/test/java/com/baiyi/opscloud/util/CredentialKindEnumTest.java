package com.baiyi.opscloud.util;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.type.CredentialKindEnum;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2021/5/17 5:01 下午
 * @Version 1.0
 */
public class CredentialKindEnumTest extends BaseUnit {

    @Test
    void toOptions() {
        System.err.println(JSON.toJSON(CredentialKindEnum.toOptions()));
    }
}
