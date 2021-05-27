package com.baiyi.caesar.util;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.types.CredentialKindEnum;
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
