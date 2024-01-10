package com.baiyi.opscloud.configuration;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.configuration.properties.WhiteConfigurationProperties;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/1 11:38 上午
 * @Version 1.0
 */
class WhiteConfigTest extends BaseUnit {

    @Resource
    private WhiteConfigurationProperties whiteConfig;

    @Test
    void whiteConfigTest() {
        print(whiteConfig);
    }

}