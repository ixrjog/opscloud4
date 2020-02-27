package com.baiyi.opscloud.encryptor;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.PasswordUtils;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2020/2/26 8:35 下午
 * @Version 1.0
 */
public class PasswordTest extends BaseUnit {

    @Test
    void test(){
        System.err.println(PasswordUtils.getRandomPW(32));
    }
}
