package com.baiyi.opscloud.config;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.config.properties.WhiteConfigurationProperties;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

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